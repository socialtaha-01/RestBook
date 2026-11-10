package com.example.restbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*

val RestRed = Color(0xFFD32F2F)
val RestDark = Color(0xFF140B0B)

private val sections = listOf(
    "Feed", "Profile", "Friends", "Groups", "Pages", "Events",
    "Watch", "Messages", "Notifications", "Feed Settings"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = RestRed,
                    onPrimary = Color.White,
                    surface = Color(0xFF241414),
                    background = RestDark
                )
            ) { RestbookApp() }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RestbookApp() {
    var selected by remember { mutableStateOf("Feed") }
    var prefs by remember { mutableStateOf(FeedPreferences()) }  // TODO: DataStore mein save karein

    NavigationDrawer(
        drawerContent = { _ ->
            Column(
                Modifier.fillMaxHeight().background(Color(0xFF1C0F0F)).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
            ) {
                Text("Restbook", color = RestRed, fontSize = 26.sp)
                sections.forEach { name ->
                    NavigationDrawerItem(
                        selected = selected == name,
                        onClick = { selected = name },
                        leadingContent = { Text(name.first().toString(), color = RestRed) }
                    ) { Text(name) }
                }
            }
        }
    ) {
        Box(Modifier.fillMaxSize().background(RestDark).padding(32.dp)) {
            when (selected) {
                "Feed" -> FeedScreen(prefs)
                "Profile" -> ProfileScreen()
                "Friends" -> ListScreen("Friends", MockData.me.friendIds.map {
                    val u = MockData.user(it); u.name to "${u.city} · ${u.work}"
                })
                "Groups" -> ListScreen("Groups", MockData.groups.map {
                    it.name to "${it.members} members · ${it.description}"
                })
                "Pages" -> ListScreen("Pages", MockData.pages.map {
                    it.name to "${it.category} · ${it.followers} followers"
                })
                "Events" -> ListScreen("Events", MockData.events.map {
                    it.title to "${it.date} · ${it.place} · ${it.going} going"
                })
                "Watch" -> ListScreen("Watch", MockData.posts.filter { it.media == MediaType.VIDEO }.map {
                    it.sourceName to it.text
                })
                "Messages" -> ListScreen("Messages", MockData.conversations.map {
                    val last = it.messages.last()
                    MockData.user(it.withUserId).name to "${last.text} · ${last.time}"
                })
                "Notifications" -> ListScreen("Notifications", MockData.notifications.map {
                    it.text to it.time
                })
                "Feed Settings" -> SettingsScreen(prefs) { prefs = it }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeedScreen(prefs: FeedPreferences) {
    val myReactions = remember { mutableStateMapOf<Int, Reaction>() }

    val posts = MockData.posts
        .filter { it.authorId !in prefs.mutedUserIds }
        .filter {
            when (it.source) {
                Source.FRIEND -> prefs.showFriends
                Source.PAGE -> prefs.showPages
                Source.GROUP -> prefs.showGroups
            }
        }
        .filter { it.media != MediaType.VIDEO || prefs.showVideos }
        .filter { it.media != MediaType.PHOTO || prefs.showPhotos }
        .sortedWith(
            compareByDescending<Post> { it.authorId in prefs.favouriteUserIds }
                .thenBy { if (prefs.sort == FeedSort.LATEST) it.minutesAgo else -it.score }
        )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (prefs.showStories) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(MockData.stories) { s ->
                        Card(onClick = {}) {
                            Column(Modifier.padding(16.dp)) {
                                Text(MockData.user(s.authorId).name, fontSize = 16.sp)
                                Text("${s.caption} · ${s.hoursLeft}h left", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
        if (posts.isEmpty()) item { Text("Feed khaali hai. Feed Settings mein filters check karein.", fontSize = 20.sp) }
        items(posts, key = { it.id }) { post ->
            val mine = myReactions[post.id]
            Card(
                onClick = {  // OK dabane par reaction cycle hota hai
                    val all = Reaction.values()
                    val next = if (mine == null) all.first() else all.getOrNull(mine.ordinal + 1)
                    if (next == null) myReactions.remove(post.id) else myReactions[post.id] = next
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(48.dp).clip(CircleShape).background(RestRed),
                            contentAlignment = Alignment.Center
                        ) { Text(post.sourceName.first().toString(), color = Color.White) }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(post.sourceName, fontSize = 20.sp)
                            Text(
                                "${post.minutesAgo}m · ${post.source.name.lowercase()} · ${post.privacy.name.lowercase()}",
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(post.text, fontSize = 22.sp)
                    if (post.media != MediaType.NONE) Text("[${post.media.name}]", color = RestRed, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    val total = post.totalReactions + if (mine != null) 1 else 0
                    Text(
                        (mine?.let { "${it.icon} ${it.label} · " } ?: "") +
                            "$total reactions · ${post.comments.size} comments · ${post.shares} shares",
                        fontSize = 16.sp
                    )
                    post.comments.firstOrNull()?.let {
                        Text("${MockData.user(it.authorId).name}: ${it.text}", fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val u = MockData.me
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(u.name, fontSize = 32.sp, color = RestRed)
        Text(u.bio, fontSize = 20.sp)
        Text("City: ${u.city}", fontSize = 18.sp)
        Text("Work: ${u.work}", fontSize = 18.sp)
        Text("Education: ${u.education}", fontSize = 18.sp)
        Text("Birthday: ${u.birthday}", fontSize = 18.sp)
        Text("Relationship: ${u.relationship}", fontSize = 18.sp)
        Text("Friends: ${u.friendIds.size}", fontSize = 18.sp)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ListScreen(title: String, rows: List<Pair<String, String>>) {
    Column {
        Text(title, fontSize = 30.sp, color = RestRed)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(rows) { (head, sub) ->
                Card(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(head, fontSize = 20.sp)
                        Text(sub, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(prefs: FeedPreferences, onChange: (FeedPreferences) -> Unit) {
    @Composable
    fun toggle(label: String, value: Boolean, set: (Boolean) -> FeedPreferences) {
        ListItem(
            selected = false,
            onClick = { onChange(set(!value)) },
            headlineContent = { Text(label) },
            trailingContent = { Switch(checked = value, onCheckedChange = null) }
        )
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text("Feed Settings (apni marzi se feed banayein)", fontSize = 28.sp, color = RestRed) }
        item { toggle("Dosto ki posts", prefs.showFriends) { prefs.copy(showFriends = it) } }
        item { toggle("Pages ki posts", prefs.showPages) { prefs.copy(showPages = it) } }
        item { toggle("Groups ki posts", prefs.showGroups) { prefs.copy(showGroups = it) } }
        item { toggle("Videos", prefs.showVideos) { prefs.copy(showVideos = it) } }
        item { toggle("Photos", prefs.showPhotos) { prefs.copy(showPhotos = it) } }
        item { toggle("Stories", prefs.showStories) { prefs.copy(showStories = it) } }
        item {
            ListItem(
                selected = false,
                onClick = {
                    onChange(prefs.copy(sort = if (prefs.sort == FeedSort.LATEST) FeedSort.TOP else FeedSort.LATEST))
                },
                headlineContent = { Text("Sort by") },
                trailingContent = { Text(if (prefs.sort == FeedSort.LATEST) "Latest" else "Top posts") }
            )
        }
        item { Spacer(Modifier.height(8.dp)); Text("Favourites (upar dikhein)", fontSize = 20.sp) }
        items(MockData.users.filter { it.id != 0 }) { u ->
            toggle(u.name, u.id in prefs.favouriteUserIds) {
                prefs.copy(favouriteUserIds = if (it) prefs.favouriteUserIds + u.id else prefs.favouriteUserIds - u.id)
            }
        }
        item { Spacer(Modifier.height(8.dp)); Text("Mute (posts na dikhein)", fontSize = 20.sp) }
        items(MockData.users.filter { it.id != 0 }) { u ->
            toggle(u.name, u.id in prefs.mutedUserIds) {
                prefs.copy(mutedUserIds = if (it) prefs.mutedUserIds + u.id else prefs.mutedUserIds - u.id)
            }
        }
    }
}

/*
 * --- build.gradle.kts (app) ---
 * implementation("androidx.tv:tv-material:1.0.0")
 * implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
 * implementation(platform("androidx.compose:compose-bom:2024.09.00"))
 * implementation("androidx.compose.ui:ui")
 * implementation("androidx.compose.foundation:foundation")
 * implementation("androidx.activity:activity-compose:1.9.2")
 *
 * --- AndroidManifest.xml ---
 * <uses-feature android:name="android.software.leanback" android:required="true" />
 * <uses-feature android:name="android.hardware.touchscreen" android:required="false" />
 * <application android:label="Restbook" ...>
 *   <activity android:name=".MainActivity" android:exported="true">
 *     <intent-filter>
 *       <action android:name="android.intent.action.MAIN" />
 *       <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
 *     </intent-filter>
 *   </activity>
 * </application>
 */
