package ivan.gorbunov.lct2024.ui.screens.client.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ivan.gorbunov.lct2024.gate.data.ChatMessage
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.launch

@Composable
fun ChatView(vm: ChatViewModel, pv: PaddingValues, uiSettings: MutableState<UiSettings>) {

    val uiState by vm.uiState.collectAsState()



    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successItemContent = { list ->

            ChatContent(
                pv
//                list
            )
        }
    )
}


@Composable
fun ChatContent(
//    messages: List<ChatMessage>
    pv: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
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
    }
    val listState = rememberLazyListState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "Чат с тренером",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .weight(1f)
        )
        LazyColumn(
            modifier = Modifier
                .weight(8f),
            state = listState
        ) {

            messages.forEach { message ->
                item {
                    MessageItem(message = message)
                }
            }

        }

        ChatInputField(
            modifier = Modifier.weight(1f),
            message = message,
            onMessageChange = { message = it },
            onSendClick = {
                messages.add(
                    ChatMessage(
                        id = messages.size.toString(),
                        text = message,
                        isUser = true
                    )
                )
                coroutineScope.launch {
                    listState.animateScrollToItem(messages.size - 1)
                }
                message = ""
            },
            onAttachClick = { /* Handle attach file click */ }
        )
    }

}