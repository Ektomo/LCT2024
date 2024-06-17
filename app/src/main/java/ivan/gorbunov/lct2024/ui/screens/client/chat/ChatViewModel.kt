package ivan.gorbunov.lct2024.ui.screens.client.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.gate.connection.ApiService
import ivan.gorbunov.lct2024.gate.connection.ChatWebSocket
import ivan.gorbunov.lct2024.gate.data.ChatMessage
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.ui.screens.auth.login.Role
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val service: ApiService,
    val chatWebSocket: ChatWebSocket,
) : BaseViewModel<Unit>() {

    var type = Role.Client
    private val _receivers = MutableStateFlow(emptyList<User>())
    val receivers: StateFlow<List<User>> = _receivers

    private val _selectedCoach = MutableStateFlow<User?>(null)
    val selectedCoach: StateFlow<User?> = _selectedCoach

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory

    private var page = 1
    private var size = 50
    private var isLoading = false

    init {
        initChat()

    }


    fun getUserId() = service.getUser().id
    fun closeSocket(){
        chatWebSocket.close()
    }
    fun connectToChat(chatId: Int) {
        chatWebSocket.close()
        chatWebSocket.connect(chatId)
        viewModelScope.launch {
            chatWebSocket.incomingMessages.collect { message ->
                _chatHistory.value += message
            }
        }
    }


    fun initChat(){
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val receivers = if (type == Role.Coach) service.getClients() else service.getTrainers()
                _receivers.value = receivers
                _selectedCoach.value = _receivers.value.firstOrNull()
                val history = service.getChatHistory( _selectedCoach.value!!.id, page, size)
                page++
                _chatHistory.value = history.reversed()
                connectToChat(service.getUser().id)
                setSuccess(Unit)
            }catch (e: Exception){
                setError(e.message ?: "Ошибка")
            }
        }
    }

    fun changeChat(coach: User){
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try{
                page = 1
                _selectedCoach.value = coach
                val history = service.getChatHistory(_selectedCoach.value!!.id, page, size)
                page++
                _chatHistory.value = history.reversed()
                connectToChat(service.getUser().id)
                setSuccess(Unit)
            }catch (e: Exception){
                setError(e.message ?: "Ошибка")
            }
        }
    }

    fun fetchChatHistory() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val history = service.getChatHistory(_selectedCoach.value!!.id, page, 50)
                _chatHistory.value += history.reversed()
                page++
                setSuccess(Unit)
            } catch (e: Exception) {
                setError(e.message ?: "Ошибка")
            } finally {
                isLoading = false
            }
        }
    }

    fun sendMsg(msg: String){
        setLoading()
        viewModelScope.launch {
            try {
                val message: ChatMessage = ChatMessage(
                    msg,
                    service.getUser().id,
                    _selectedCoach.value!!.id
                )
                chatWebSocket.sendMessage(message)
                _chatHistory.value += message
                setSuccess(Unit)
            } catch (e: Exception) {
                setError(e.message ?: "Ошибка")
            }
        }
    }




}