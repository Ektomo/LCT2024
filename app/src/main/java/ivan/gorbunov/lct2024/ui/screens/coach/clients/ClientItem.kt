package ivan.gorbunov.lct2024.ui.screens.coach.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.gate.data.User

@Composable
fun ClientItem(client: User, onMenuItemClick: (DropdownClientChoose, User) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model =
//                (client.url?.ifEmpty {  } ?:
                "http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg",
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = client.name + " " + client.surname,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onMenuItemClick(DropdownClientChoose.CHAT, client)
                    }, text = {
                        Text("Открыть чат")
                    })


//                    DropdownMenuItem(onClick = {
//                        expanded = false
//                        onMenuItemClick(DropdownClientChoose.HISTORY, client)
//                    }, text = {
//                        Text("Открыть историю")
//                    })
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onMenuItemClick(DropdownClientChoose.PROGRESS, client)
                    }, text = {
                        Text("Прогресс")
                    })
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onMenuItemClick(DropdownClientChoose.ASSIGN, client)
                    }, text = {
                        Text("Назначить тренировку")
                    })
                }
            }
        }
    }
}

enum class DropdownClientChoose{
    ASSIGN, PROGRESS, CHAT, HISTORY
}