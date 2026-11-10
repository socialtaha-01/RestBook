package com.example.restbook

/** Abhi sara data mock hai. Real data ke liye backend/API se replace karein. */
object MockData {
    val me = User(
        0, "Aap (You)", "Restbook par naye!", "Karachi", "Software Developer",
        "University of Karachi", "1 Jan 1995", "Single", listOf(1, 2, 3, 4)
    )

    val users = listOf(
        me,
        User(1, "Amina Khan", "Runner & reader", "Lahore", "Designer", "NCA", "12 Mar 1996", "Single", listOf(0, 2)),
        User(2, "Daniel Reyes", "Home cook", "Dubai", "Chef", "Culinary Institute", "5 Jul 1993", "Married", listOf(0, 1)),
        User(3, "Sofia Lindqvist", "Photographer", "Stockholm", "Freelancer", "KTH", "21 Sep 1994", "Single", listOf(0)),
        User(4, "Omar Farouk", "Football fan", "Cairo", "Engineer", "Cairo University", "30 Nov 1992", "Engaged", listOf(0))
    )

    fun user(id: Int) = users.first { it.id == id }

    val posts = listOf(
        Post(
            1, 1, Source.FRIEND, "Amina Khan", "Pehli marathon complete ki! Sab ka shukriya.",
            MediaType.PHOTO, Privacy.FRIENDS, 120,
            mapOf(Reaction.LIKE to 90, Reaction.LOVE to 30, Reaction.WOW to 8),
            listOf(Comment(1, 2, "Mubarak ho!", 100, 4), Comment(2, 3, "Zabardast!", 90, 2)), 3
        ),
        Post(
            2, 2, Source.FRIEND, "Daniel Reyes", "Aaj raat homemade ramen. Recipe chahiye?",
            MediaType.VIDEO, Privacy.PUBLIC, 240,
            mapOf(Reaction.LIKE to 50, Reaction.HAHA to 12, Reaction.LOVE to 25),
            listOf(Comment(3, 1, "Haan please!", 200, 1)), 6
        ),
        Post(
            3, 10, Source.PAGE, "Tech Daily", "Android TV ke liye naye Compose APIs aa gaye.",
            MediaType.NONE, Privacy.PUBLIC, 300,
            mapOf(Reaction.LIKE to 340, Reaction.WOW to 40),
            listOf(Comment(4, 4, "Useful!", 280, 9)), 55
        ),
        Post(
            4, 3, Source.GROUP, "Photography Lovers", "Chhat se aaj ka sunset.",
            MediaType.PHOTO, Privacy.PUBLIC, 360,
            mapOf(Reaction.LOVE to 210, Reaction.LIKE to 120, Reaction.WOW to 33),
            emptyList(), 14
        ),
        Post(
            5, 4, Source.FRIEND, "Omar Farouk", "Match dekhne aa rahe ho aaj raat?",
            MediaType.NONE, Privacy.FRIENDS, 1440,
            mapOf(Reaction.LIKE to 40, Reaction.HAHA to 10),
            listOf(Comment(5, 0, "Pakka aaunga", 1400, 0)), 1
        )
    )

    val stories = listOf(
        Story(1, 1, "Marathon day", 5),
        Story(2, 2, "Ramen time", 12),
        Story(3, 3, "Golden hour", 20)
    )

    val groups = listOf(
        Group(1, "Photography Lovers", 12400, "Apni behtareen photos share karein"),
        Group(2, "Android TV Devs", 5300, "TV app development discussion")
    )

    val pages = listOf(
        Page(10, "Tech Daily", "News & Media", 120000),
        Page(11, "Karachi Foodies", "Restaurant Guide", 45000)
    )

    val events = listOf(
        Event(1, "Football Night", "Sat 8:00 PM", "Omar ka ghar", 14),
        Event(2, "Dev Meetup", "Sun 5:00 PM", "Karachi Tech Hub", 60)
    )

    val conversations = listOf(
        Conversation(1, 1, listOf(ChatMessage(1, "Kal milte hain?", "10:02"), ChatMessage(0, "Haan zaroor", "10:05"))),
        Conversation(2, 2, listOf(ChatMessage(2, "Recipe bhej deta hoon", "Yesterday")))
    )

    val notifications = listOf(
        AppNotification(1, "Amina Khan ne aapki post par comment kiya", "5m"),
        AppNotification(2, "Daniel Reyes ne aapko event mein invite kiya", "1h"),
        AppNotification(3, "Photography Lovers mein 3 nayi posts", "3h")
    )
}
