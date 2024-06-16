package ivan.gorbunov.lct2024.ui.screens.client.training.exercise_list

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.ui.screens.client.training.TrainingCrossfadeState
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.ExerciseContent
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.FinishTrainingScreen
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.RestScreen
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.WarmUpContent
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.WarmDownContent
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.WarmUpEndContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.core.TakePhoto
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings


@Composable
fun TrainingView(
    vm: TrainingViewModel,
    pv: PaddingValues,
    uiSettings: MutableState<UiSettings>,
    navController: NavController
) {

    val uiState by vm.uiState.collectAsState()
    val timer by vm.timer.collectAsState()
    val restTimer by vm.restTimer.collectAsState()
    val isStarted by vm.isStarted.collectAsState()
    val completedExercises by vm.completedExercises.collectAsState()
    val size = vm.trainCount()
    val trainingState = vm.getCurTrainingState()
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var photo by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    uiSettings.value = UiSettings()

    if (showDialog){
        ConfirmFinishTrainingDialog(onConfirm = {
            navController.popBackStack()
            showDialog = false
        }) {
            showDialog = false
        }
    }


    TrainingCrossfadeState(
        uiState = uiState,
        modifier = Modifier
            .padding(pv)
            .padding(horizontal = 8.dp),
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        trainingContent = { training ->
            TrainingOverview(
                uiSettings,
                training,
                onBottomClick = {
                    vm.setWarmUp()
                }, onExerciseClick = {
                    vm.goToExerciseWith(it)
                })
        },
        exerciseContent = {
            ExerciseContent(
                exercise = it,
                timer = timer,
                exerciseIndex = vm.getCurIdx(),
                totalExercises = size,
                isStarted = isStarted,
                curTrainingState = trainingState,
                completedExercises = completedExercises,
                onNextClick = {
//                    vm.nextExercise()
                },
                onPreviousClick = {
//                    vm.previousExercise()
                },
                onCompleteClick = { vm.completeExercise() },
                onStartTrainingClick = { vm.setWarmUp() },
                onCloseClick = {
                    if (isStarted) {
                        showDialog = true
                    }else{
                        vm.goToList()
                    }

                },
                trainingStarted = isStarted
            )
        },
        restContent = { cur, next ->
            RestScreen(
                exercise = cur,
                nextExercise = next,
                timer = restTimer,
                onSkipClick = {
                    vm.nextExercise()
                },
                onValueChange = {

                },
                onWeightChange = {

                })
        },
        warmUpContent = {
            WarmUpContent(
                onStartClick = {
                    vm.startWarmUp()
                },
                onSkipClick = {
                    vm.startTrainee()
                })
        },
        warmDownContent = {
            WarmDownContent(
                it,
                onValueChange = {

                },
                onWeightChange = {

                },
                onStartClick = {
                    vm.startWarmDown()
                },
                onSkipClick = {
                    vm.finishTrainee()
                })
        },
        warmUpEndContent = {
            WarmUpEndContent {
                vm.startTrainee()
            }
        },
        finishContent = {
            FinishTrainingScreen(
                onPhotoClick = { vm.setTakePhoto() },
                onMoodSelected = { mood -> selectedMood = mood },
                selectedMood = selectedMood,
                photo = photo,
                onWeightEntered = {},
                onFinishClick = {
                    vm.finishTraining {
                        navController.popBackStack()
                    }
                },
                onSendPhoto = {

                }
            )
        },
        takePhoto = {
            TakePhoto {
                photo = it
                vm.setFinish()
            }
        },
        onBack = {
            vm.previous(
                onNeedFinish = {
                    showDialog = true
                },
                onPopUp = {
                    navController.popBackStack()
                })
        }
    )
}

@Composable
fun TrainingOverview(
    uiSettings: MutableState<UiSettings>,
    training: Training,
    onBottomClick: () -> Unit,
    onExerciseClick: (Int) -> Unit,
    isEnabled: Boolean = true,
    isCoach: Boolean = false
) {
    LaunchedEffect(key1 = Unit) {
        uiSettings.value = UiSettings()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = training.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.TopStart)
        )
        ExerciseList(
            modifier = Modifier.padding(top = 48.dp),
            exercises = training.exercises,
            isEnabled = isEnabled,
            onExerciseClick = onExerciseClick
        )
        Button(
            onClick = onBottomClick, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = if (isCoach) "Редактировать" else "Начать тренировку")
        }
    }
}

@Composable
fun ExerciseList(modifier: Modifier, exercises: List<Exercise>, isEnabled: Boolean = true, onExerciseClick: (Int) -> Unit) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        exercises.forEachIndexed { _, exercise ->
            ExerciseItem(exercise = exercise, isEnabled = isEnabled, onClick = { onExerciseClick(exercise.id) })
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, isEnabled: Boolean = true, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .also {
                if (isEnabled) {
                    it.clickable(onClick = onClick)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = (exercise.photos.firstOrNull() ?: ""),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Мышцы: ${exercise.muscle}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Сложность: ${exercise.difficulty}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun getRandomMotivationalQuote(): String {
    val quotes = listOf(
        "Никогда не сдавайся, потому что это всего лишь начало.",
        "Вы сильнее, чем вы думаете. Продолжайте!",
        "Трудные времена создают сильных людей. Не сдавайтесь!"
    )
    return quotes.random()
}

@Composable
fun ConfirmFinishTrainingDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val motivationalQuote = remember { getRandomMotivationalQuote() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Закончить тренировку?") },
        text = {
            Text(
                text = "Не все упражнения выполнены. $motivationalQuote"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Да")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Нет")
            }
        }
    )
}