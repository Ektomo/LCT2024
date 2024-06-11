package ivan.gorbunov.lct2024.ui.screens.client.progress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ivan.gorbunov.lct2024.gate.data.ChartData
import ivan.gorbunov.lct2024.gate.data.Comment
import ivan.gorbunov.lct2024.gate.data.Parameter
import ivan.gorbunov.lct2024.gate.data.Quote
import ivan.gorbunov.lct2024.gate.data.parameters
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings

@Composable
fun ProgressView(vm: ProgressViewModel, paddingValues: PaddingValues, uiSettings: MutableState<UiSettings>){

    val uiState by vm.uiState.collectAsState()
    val selectedChip by vm.selectedChip.collectAsState()

    DisposableEffect(key1 = Unit) {
        uiSettings.value = UiSettings(
            fabContent = {
                FloatingActionButton(onClick = {

                }) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Выбрать временной промежуток")
                }
            }
        )
        onDispose {
            uiSettings.value = UiSettings()
        }
    }



    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successListContent = { list ->
            ProgressContent(pv = paddingValues, data = list, selectedChip) {
                vm.updateFilterChip(it)
            }
        }
    )


}

@Composable
fun ProgressContent(pv: PaddingValues, data: List<ChartData>, selectedParameter: Parameter, onParameterSelected: (Parameter) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Прогресс",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // График
        LineChartWithAxes(data, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
//            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Чипсы параметров
        ParameterChips(parameters, selectedParameter) { parameter ->
            onParameterSelected(parameter)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Мотивирующие цитаты
        Text(text = "Цитаты дня", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
        listOf(
            Quote("Не останавливайся, пока не станет легче."),
            Quote("Каждый день - новый шанс измениться."),
            Quote("Сила не в мышцах, а в характере.")
        ).forEach { quote ->
            QuoteCard(quote = quote)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Комментарии тренеров
        Text(text = "Комментарии тренеров", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
        listOf(
            Comment("Отличный прогресс, продолжай в том же духе!", "Тренер Алексей"),
            Comment("Результаты впечатляют, так держать!", "Тренер Марина"),
            Comment("Ты на верном пути, не сдавайся!", "Тренер Иван")
        ).forEach { comment ->
            CommentCard(comment = comment)
        }
    }
}

@Composable
fun QuoteCard(quote: Quote) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = quote.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun CommentCard(comment: Comment) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"${comment.text}\"",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "- ${comment.author}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}