package ivan.gorbunov.lct2024.gate.data

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val message: String,
    val senderId: Int,
    val receiverId: Int,
    val filePath: String? = null
)

@Serializable
data class ChatHistory(
    val messages: List<ChatMessage>
)