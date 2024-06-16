package ivan.gorbunov.lct2024.ui.screens.coach.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_list.TrainingOverview
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.CoachNavItem
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings

@Composable
fun TrainingListView(
    navController: NavController,
    vm: TrainingListViewModel,
    pv: PaddingValues,
    uiSettings: MutableState<UiSettings>
) {

    val uiState by vm.uiState.collectAsState()
    var onBackValue by remember {
        mutableIntStateOf(1)
    }

//    LaunchedEffect(key1 = onBackValue) {
    uiSettings.value = UiSettings(
        fabContent = {
            FloatingActionButton(onClick = {
                navController.navigate(CoachNavItem.CreateWorkouts.screen_route)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить тренировку"
                )
            }
        }
    )
//    }

    CrossfadeState(
        uiState = uiState,
        modifier = Modifier
            .padding(pv)
            .padding(8.dp),
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successListContent = { list ->
            TrainingListScreen(trainings = list, onTrainingClick = {
                vm.goToTrainWith(it.id)
            })
        },
        successItemContent = { training ->
            TrainingOverview(
                uiSettings,
                training,
                onBottomClick = {},
                isEnabled = false,
                isCoach = true,
                onExerciseClick = {

                })
        },
        onBack = {
            onBackValue += 1
            vm.back {
                navController.popBackStack()
            }
        }
    )
}

@Composable
fun TrainingListScreen(
    trainings: List<Training>,
    onTrainingClick: (Training) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    val filteredTrainings = trainings.filter {
        it.name.contains(searchText, ignoreCase = true)
    }


    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Поиск по имени") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск")
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Сброс")
                    }
                }
            }
        )
        LazyColumn {
            filteredTrainings.forEach { training ->
                item {
                    TrainingListItem(
                        training = training,
                        onClick = { onTrainingClick(training) })
                }
            }
        }
    }

}

@Composable
fun TrainingListItem(training: Training, onClick: () -> Unit) {
    val firstExerciseWithImage = training.exercises.firstOrNull { it.photos.isNotEmpty() }
    val imageUrl = firstExerciseWithImage?.photos?.firstOrNull()?.url
        ?: "http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = training.name,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Дата: ${training.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            Text(
                text = training.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Упражнения: ${training.exercises.size}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}




