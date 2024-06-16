package ivan.gorbunov.lct2024.ui.screens.client.workout

import android.content.Context
import android.os.Build
import android.util.Range
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.gate.data.WorkoutItem
import ivan.gorbunov.lct2024.ui.screens.coach.training.TrainingListItem
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.ClientNavItem
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutView(navController: NavHostController, vm: WorkoutViewModel, pv: PaddingValues, uiSettings: MutableState<UiSettings>) {

    val uiState by vm.uiState.collectAsState()


//    DisposableEffect(key1 = Unit) {
//        uiSettings.value = UiSettings(
//            fabContent = {
//                FloatingActionButton(onClick = {
//
//                }) {
//                    Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
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
        successItemContent = { workouts ->
            val context = LocalContext.current
            var startDate by remember { mutableStateOf<LocalDate?>(null) }
            var endDate by remember { mutableStateOf<LocalDate?>(null) }
            val calendarState = rememberSheetState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv)
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Тренировки",
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    workouts.forEach { workout ->
                        TrainingListItem(workout) {
                            navController.navigate(ClientNavItem.Training.screen_route)
                        }
                    }
                }

//                val range: Range<LocalDate>? =

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
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toLocalizedDateString(context: Context): String {
    val date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    val dateFormat = android.text.format.DateFormat.getDateFormat(context)
    return dateFormat.format(date)
}

//@Composable
//fun WorkoutCard(workout: Training, onClick: (Training) -> Unit) {
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 16.dp)
//            .clickable(onClick = { onClick(workout) })
//    ) {
//        Column {
//            Image(
//                painter = rememberAsyncImagePainter(workout.imageUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
//                contentScale = ContentScale.Crop
//            )
//            Column(modifier = Modifier.padding(8.dp)) {
//                Text(
//                    text = workout.date,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = workout.name,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//                Text(
//                    text = workout.description,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}