package ivan.gorbunov.lct2024.gate.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val message: String,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("receiver_id")
    val receiverId: Int,
    val filePath: String? = null
)

@Serializable
data class ChatMessage2(
    @SerialName("content")
    val content: String,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("receiver_id")
    val receiverId: Int,
    val filePath: String? = null
)

@Serializable
data class ChatHistory(
    val messages: List<ChatMessage2>
)