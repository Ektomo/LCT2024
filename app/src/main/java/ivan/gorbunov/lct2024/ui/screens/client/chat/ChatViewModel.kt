package ivan.gorbunov.lct2024.ui.screens.client.chat

import androidx.compose.runtime.mutableStateListOf
import ivan.gorbunov.lct2024.gate.data.ChatMessage
import ivan.gorbunov.lct2024.gate.data.HomeItem
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import javax.inject.Inject

class ChatViewModel @Inject constructor(

) : BaseViewModel<List<ChatMessage>>() {

    init {
        setSuccess(
            listOf(
                ChatMessage(id = "1", text = "Привет!", isUser = false),
                ChatMessage(id = "2", text = "Здравствуйте! Как ваши дела?", isUser = true),
                ChatMessage(id = "3", text = "Хорошо, спасибо! Как у вас?", isUser = false),
                ChatMessage(
                    id = "4",
                    text = "Тоже хорошо. Готовы к следующей тренировке?",
                    isUser = true
                ),
                ChatMessage(id = "5", text = "Да, жду не дождусь!", isUser = false)
            )
        )
    }


}