package otus.gpb.recyclerview


data class ChatItem(
    val id: Int,
    val checkMark: String,
    val isPinned: Boolean,
    val soundIsOn: Boolean,
    val isMentioned: Boolean,
    val date: String,
    val title: String,
    val author: String,
    val messageLast: String,
    val image: Int,
)
