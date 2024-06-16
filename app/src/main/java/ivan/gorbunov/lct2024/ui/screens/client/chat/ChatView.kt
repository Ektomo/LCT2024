package ivan.gorbunov.lct2024.ui.screens.client.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ivan.gorbunov.lct2024.gate.data.ChatMessage
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.core.UiState
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.launch

@Composable
fun ChatView(
    vm: ChatViewModel,
    pv: PaddingValues,
    uiSettings: MutableState<UiSettings>,
    isCoach: Boolean = false
) {

    val uiState by vm.uiState.collectAsState()
    val msgs by vm.chatHistory.collectAsState()
    val selectedCoach by vm.selectedCoach.collectAsState()
    val coaches by vm.receivers.collectAsState()
//    LaunchedEffect(key1 = Unit) {
        uiSettings.value = UiSettings()
//    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            vm.closeSocket()
        }
    }


    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successItemContent = {
            ChatContent(
                msgs,
                pv,
                coaches,
                selectedCoach,
                isCoach,
                uiState,
                userId = vm.getUserId(),
                onSendClick = {
                        vm.sendMsg(it)
                },
                onPage = {
                    vm.fetchChatHistory()
                }
            ){
                vm.changeChat(it)
            }
        }
    )
}


@Composable
fun ChatContent(
    messages: List<ChatMessage>,
    pv: PaddingValues,
    coaches: List<User>,
    selectedCoach: User?,
    isCoach: Boolean,
    uiState: UiState<Unit>,
    userId: Int,
    onSendClick: (String) -> Unit,
    onPage: () -> Unit,
    onSelectCoach: (User) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var message by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == messages.size - 1) {
                    onPage()
                }
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(horizontal = 8.dp)
    ) {
        ChatHeaderRow(
            modifier = Modifier
                .weight(1f),
            title = "Чат с ${if (isCoach) "клиентом" else "тренером"}",
            names = coaches,
            selectedName = selectedCoach!!
        ) {
            onSelectCoach(it)
        }

        LazyColumn(
            modifier = Modifier
                .weight(8f),
            state = listState
        ) {

            messages.forEach { message ->
                item {
                    MessageItem(message = message, userId)
                }
                item {
                    // Show loading indicator at the end of the list when new data is being fetched
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }

        }

        ChatInputField(
            modifier = Modifier.weight(1f),
            message = message,
            onMessageChange = { message = it },
            onSendClick = {
                onSendClick(message)
                coroutineScope.launch {
                    if(messages.isNotEmpty()) {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }
                message = ""
            },
            onAttachClick = { /* Handle attach file click */ }
        )
    }

}

@Composable
fun ChatHeaderRow(
    modifier: Modifier,
    title: String,
    names: List<User>,
    selectedName: User,
    onNameSelected: (User) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        Box {
            TextButton(onClick = { expanded = true }) {
                Text(text = selectedName.name + " " + selectedName.surname)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                names.forEach { name ->
                    DropdownMenuItem(
                        onClick = {
                            onNameSelected(name)
                            expanded = false
                        },
                        text = {
                            Text(text = name.name + " " + name.surname)
                        }
                    )
                }
            }
        }
    }
}