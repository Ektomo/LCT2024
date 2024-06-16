package ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_list.TrainState

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ExerciseContent(
    exercise: Exercise,
    timer: String,
    exerciseIndex: Int,
    totalExercises: Int,
    isStarted: Boolean,
    curTrainingState: TrainState,
    completedExercises: Set<Int>,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onStartTrainingClick: () -> Unit,
    onCloseClick: () -> Unit,
    trainingStarted: Boolean
) {

    val swipeableState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, 1f to 1)
    LaunchedEffect(swipeableState.offset.value) {
        if (swipeableState.offset.value > 0.5f) {
            onNextClick()
            swipeableState.snapTo(0)
        } else if (swipeableState.offset.value < -0.5f) {
            onPreviousClick()
            swipeableState.snapTo(0)
        }
    }

    var state = WorkoutState.Weight

    var weight by remember {
        mutableIntStateOf(0)
    }
    if (exercise.plannedWeight != null) {
        state = WorkoutState.Weight
        weight = exercise.plannedWeight
    } else {
        state = WorkoutState.Self
    }

    var value by remember {
        mutableIntStateOf(exercise.plannedRepetitions ?: 0)
    }
    if (weight > 0) {
        value = exercise.plannedRepetitions ?: 0
    } else {
        value = exercise.plannedDuration ?: 0
        if (value == 0) {
            exercise.plannedDistance ?: 0
            state = WorkoutState.Distance
        } else {
            state = WorkoutState.Duration
        }
    }

    val t = when (state) {
        WorkoutState.Distance -> "метров"
        WorkoutState.Weight, WorkoutState.Self -> "повторений"
        WorkoutState.Duration -> "секунд"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Индикатор выполнения упражнений
        if (curTrainingState == TrainState.Main) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 0 until totalExercises) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                when {
                                    i == exerciseIndex -> Color.Blue
                                    completedExercises.contains(i) -> Color.Green
                                    else -> Color.Gray
                                }
                            )
                            .padding(horizontal = 2.dp)
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }
        // Крестик для закрытия
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Закрыть")
        }
        // Основное содержимое
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .swipeable(
//                    state = swipeableState,
//                    anchors = anchors,
//                    orientation = Orientation.Horizontal,
//                    thresholds = { _, _ -> FractionalThreshold(0.5f) }
//                )
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (isStarted) {
                Text(
                    text = "Время: $timer",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            LazyRow {
                exercise.photos.forEach { photo ->
                    item {
                        AsyncImage(
                            model = photo.url.replace("8000", "5000"),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Цели упражнения",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Цели упражнения",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "$value $t ${if (state == WorkoutState.Weight) " с весом $weight кг" else ""}.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(
                    text = "Информация о упражнении:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Основная мышца: ${exercise.muscle}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Дополнительная мышца: ${exercise.additionalMuscle}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Тип: ${exercise.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Оборудование: ${exercise.equipment}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Сложность: ${exercise.difficulty}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }


        }
        Button(
            onClick = if (trainingStarted) onCompleteClick else onStartTrainingClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = if (trainingStarted) "Выполнить упражнение" else "Начать тренировку")
        }
    }
}