package com.example.restbook

enum class Reaction(val label: String, val icon: String) {
    LIKE("Like", "👍"), LOVE("Love", "❤️"), HAHA("Haha", "😂"),
    WOW("Wow", "😮"), SAD("Sad", "😢"), ANGRY("Angry", "😡")
}

enum class Privacy { PUBLIC, FRIENDS, ONLY_ME }
enum class Source { FRIEND, PAGE, GROUP }
enum class MediaType { NONE, PHOTO, VIDEO }
enum class FeedSort { LATEST, TOP }

data class User(
    val id: Int,
    val name: String,
    val bio: String,
    val city: String,
    val work: String,
    val education: String,
    val birthday: String,
    val relationship: String,
    val friendIds: List<Int>
)

data class Comment(
    val id: Int,
    val authorId: Int,
    val text: String,
    val minutesAgo: Int,
    val likes: Int
)

data class Post(
    val id: Int,
    val authorId: Int,
    val source: Source,
    val sourceName: String,          // friend name, page name or group name
    val text: String,
    val media: MediaType,
    val privacy: Privacy,
    val minutesAgo: Int,
    val reactions: Map<Reaction, Int>,
    val comments: List<Comment>,
    val shares: Int
) {
    val totalReactions get() = reactions.values.sum()
    val score get() = totalReactions + comments.size * 3 + shares * 5
}

data class Story(val id: Int, val authorId: Int, val caption: String, val hoursLeft: Int)
data class Group(val id: Int, val name: String, val members: Int, val description: String)
data class Page(val id: Int, val name: String, val category: String, val followers: Int)
data class Event(val id: Int, val title: String, val date: String, val place: String, val going: Int)
data class ChatMessage(val fromId: Int, val text: String, val time: String)
data class Conversation(val id: Int, val withUserId: Int, val messages: List<ChatMessage>)
data class AppNotification(val id: Int, val text: String, val time: String)

/** Yeh user ki apni feed settings hain. */
data class FeedPreferences(
    val showFriends: Boolean = true,
    val showPages: Boolean = true,
    val showGroups: Boolean = true,
    val showVideos: Boolean = true,
    val showPhotos: Boolean = true,
    val showStories: Boolean = true,
    val sort: FeedSort = FeedSort.LATEST,
    val favouriteUserIds: Set<Int> = emptySet(),   // inki posts upar aayengi
    val mutedUserIds: Set<Int> = emptySet()        // inki posts nahi dikhengi
)
