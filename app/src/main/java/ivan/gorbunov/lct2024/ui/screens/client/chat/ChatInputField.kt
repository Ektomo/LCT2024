package ivan.gorbunov.lct2024.ui.screens.client.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputField(
    modifier: Modifier,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            placeholder = { Text("Отправить сообщение") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon ={
                IconButton(
                    onClick = if (message.isEmpty()) onAttachClick else onSendClick,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = if (message.isEmpty()) Icons.Default.AttachFile else Icons.Default.Send,
                        contentDescription = if (message.isEmpty()) "Прикрепить файл" else "Отправить",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}