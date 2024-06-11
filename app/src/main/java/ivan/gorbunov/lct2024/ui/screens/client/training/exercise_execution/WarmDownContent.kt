package ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.R
import ivan.gorbunov.lct2024.gate.data.Exercise

@Composable
fun WarmDownContent(
    lastExercise: Exercise,
    onWeightChange: (Int) -> Unit,
    onValueChange: (Int) -> Unit,
    onStartClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Верхняя часть с изображением
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = R.drawable.warmdown,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
        }

        // Текст описания и кнопки
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Сделаем растяжку?\nРасслабит ваши мышцы и повысит гибкость.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Начать растяжку")
                }
                TextButton(
                    onClick = onSkipClick
                ) {
                    Text(text = "Закончить тренировку")
                }
                lastExercise.let { exercise ->

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


//                Text(
//                    text = "В предыдущем упражнении ваша цель была: $value  " +
//                            when(state){
//                                WorkoutState.Distance -> "метров"
//                                WorkoutState.Weight, WorkoutState.Self -> "повторений"
//                                WorkoutState.Duration -> "секунд"
//                            } +
//                            "${if (state == WorkoutState.Weight) " с весом $weight кг, укажите ваши фактические результаты" else ""}.",
//                    style = MaterialTheme.typography.bodyLarge,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
                    Text(
                        text = "Подтвердите результаты предыдущего подхода",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        var factValue by remember {
                            mutableIntStateOf(value)
                        }

                        OutlinedTextField(
                            value = if (factValue == 0) "" else "$factValue",
                            onValueChange = {
                                val fValue = it.toIntOrNull() ?: 0
                                onValueChange(fValue)
                                factValue = fValue
                            },
                            label = { Text("Фактические повторения") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.weight(1f)
                        )
                        if (state == WorkoutState.Weight) {
                            var factWeight by remember {
                                mutableIntStateOf(weight)
                            }
                            Spacer(modifier = Modifier.weight(0.1f))
                            OutlinedTextField(
                                value = "$factWeight",
                                onValueChange = {
                                    val fWeight = it.toIntOrNull() ?: 0
                                    onWeightChange(fWeight)
                                    factWeight = fWeight
                                },
                                label = { Text("Фактический вес") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

            }
        }
    }
}