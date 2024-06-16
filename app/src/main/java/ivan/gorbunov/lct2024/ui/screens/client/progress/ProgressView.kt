package ivan.gorbunov.lct2024.ui.screens.client.progress

import android.os.Build
import android.util.Range
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import ivan.gorbunov.lct2024.gate.data.ChartData
import ivan.gorbunov.lct2024.gate.data.Comment
import ivan.gorbunov.lct2024.gate.data.Parameter
import ivan.gorbunov.lct2024.gate.data.Quote
import ivan.gorbunov.lct2024.gate.data.parameters
import ivan.gorbunov.lct2024.ui.screens.client.workout.toLocalizedDateString
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgressView(vm: ProgressViewModel, paddingValues: PaddingValues, uiSettings: MutableState<UiSettings>, isCoach: Boolean = false){

    val uiState by vm.uiState.collectAsState()
    val selectedChip by vm.selectedChip.collectAsState()

//    DisposableEffect(key1 = Unit) {
//        uiSettings.value = UiSettings(
//            fabContent = {
//                FloatingActionButton(onClick = {
//
//                }) {
//                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Выбрать временной промежуток")
//                }
//            }
//        )
//        onDispose {
            uiSettings.value = UiSettings()
//        }
//    }



    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successListContent = { list ->
            ProgressContent(pv = paddingValues, data = list, isCoach = isCoach, selectedParameter = selectedChip) {
                vm.updateFilterChip(it)
            }
        }
    )


}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressContent(pv: PaddingValues, data: List<ChartData>, selectedParameter: Parameter, isCoach: Boolean = false, onParameterSelected: (Parameter) -> Unit) {

    val context = LocalContext.current
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Period(
            selectedRange = if (startDate == null || endDate == null) null else Range(startDate, endDate)
        ) { start, end ->
            startDate = start
            endDate = end
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Прогресс",
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = {
                calendarState.show()
            }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Календарь"
                )
            }
        }

        if (startDate != null && endDate != null) {
            Text(
                text = "Период: ${startDate!!.toLocalizedDateString(context)} - ${endDate!!.toLocalizedDateString(context)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }


        // График
        LineChartWithAxes(data, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
//            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(start = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Чипсы параметров
        FilterChips(parameters, selectedParameter) { parameter ->
            onParameterSelected(parameter)
        }

        if (isCoach){
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Добавить комментарий", color = MaterialTheme.colorScheme.background)
            }

        }else {

            Spacer(modifier = Modifier.height(16.dp))

            // Мотивирующие цитаты
            Text(
                text = "Цитаты дня",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            listOf(
                Quote("Не останавливайся, пока не станет легче."),
                Quote("Каждый день - новый шанс измениться."),
                Quote("Сила не в мышцах, а в характере.")
            ).forEach { quote ->
                QuoteCard(quote = quote)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Комментарии тренеров
            Text(
                text = "Комментарии тренеров",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            listOf(
                Comment("Отличный прогресс, продолжай в том же духе!", "Тренер Алексей"),
                Comment("Результаты впечатляют, так держать!", "Тренер Марина"),
                Comment("Ты на верном пути, не сдавайся!", "Тренер Иван")
            ).forEach { comment ->
                CommentCard(comment = comment)
            }
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