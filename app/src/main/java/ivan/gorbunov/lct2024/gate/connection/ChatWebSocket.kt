package ivan.gorbunov.lct2024.gate.connection

import ivan.gorbunov.lct2024.gate.data.ChatMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocket(
    private val client: OkHttpClient,
    private val baseUrl: String
) {
    private val json = JsonSerializer.format
    private val _incomingMessages = Channel<ChatMessage>()
    val incomingMessages: Flow<ChatMessage> = _incomingMessages.receiveAsFlow()

    private var webSocket: WebSocket? = null

    fun connect(chatId: Int) {
        val request = Request.Builder().url("$baseUrl/chats/$chatId").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val message = json.decodeFromString<ChatMessage>(text)
                _incomingMessages.trySend(message).isSuccess
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                println(t.message)
            }
        })
    }

    fun sendMessage(message: ChatMessage) {
        webSocket?.send(json.encodeToString(ChatMessage.serializer(), message))
    }

    fun close() {
        webSocket?.close(1000, "Closing connection")
    }
}