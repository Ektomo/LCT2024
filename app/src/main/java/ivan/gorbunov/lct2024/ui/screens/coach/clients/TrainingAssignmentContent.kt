package ivan.gorbunov.lct2024.ui.screens.coach.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.WorkoutState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TrainingAssignmentContent(viewModel: ClientsViewModel, onAssign: (Training) -> Unit) {
//    val trainings by viewModel.trainings.collectAsState()
    val training by viewModel.selectedTraining.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val roundCount by viewModel.roundCount.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }




    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            training?.name ?: "Выберите тренировку",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Дата тренировки: ")
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = selectedDate ?: "",
                onValueChange = {},
                label = { Text("Выберите дату") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Выбрать дату"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = (roundCount ?: "").toString(),
            onValueChange = { count ->
                count.toIntOrNull().let { viewModel.updateRoundCount(it) }
            },
            label = { Text("Количество кругов") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                viewModel.setSelectTrain()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (training == null) "Выбрать тренировку" else "Заменить тренировку")
        }


        Spacer(modifier = Modifier.height(16.dp))

        training?.let { training ->

            training.exercises.forEach { exercise ->
                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                    item {
                        Text(exercise.name, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "На прошлой тренировке: ${exercise.actualRepetitions ?: "нет данных"}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            when (exercise.type) {
                                WorkoutState.Weight -> {
                                    WeightInputFields(exercise) { e, v, s ->
                                        viewModel.updateExerciseValue(e.id, v, s)
                                    }
                                }

                                WorkoutState.Distance -> {
                                    DistanceInputFields(exercise) { e, v, s ->
                                        viewModel.updateExerciseValue(e.id, v, s)
                                    }
                                }

                                WorkoutState.Duration -> {
                                    DurationInputFields(exercise) { e, v, s ->
                                        viewModel.updateExerciseValue(e.id, v, s)
                                    }
                                }

                                WorkoutState.Self -> {
                                    SelfInputFields(exercise) { e, v, s ->
                                        viewModel.updateExerciseValue(e.id, v, s)
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedDate.isNullOrEmpty()) {
                        // Handle empty date error
                    } else if (training.exercises.any { it.plannedRepetitions == null || it.plannedRepetitions <= 0 }) {
                        // Handle validation error for exercises
                    } else {
                        onAssign(training)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назначить тренировку")
            }
        }
    }



    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                viewModel.updateSelectedDate(date)
                showDatePicker = false
            },
            onDismissRequest = { showDatePicker = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selectedDateMillis = datePickerState.selectedDateMillis
                val selectedDate = selectedDateMillis?.let { millis ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = millis
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    dateFormat.format(calendar.time)
                }
                if (selectedDate != null) {
                    onDateSelected(selectedDate)
                }
                onDismissRequest()
            }) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun WeightInputFields(exercise: Exercise, onUpdate: (Exercise, Int, String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {


        OutlinedTextField(
            value = exercise.plannedRepetitions?.toString() ?: "",
            onValueChange = { value ->
                value.toIntOrNull()?.let { onUpdate(exercise, it, "repetitions") }
            },
            label = { Text("Повторения") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        OutlinedTextField(
            value = exercise.plannedWeight?.toString() ?: "",
            onValueChange = { value ->
                value.toIntOrNull()
                    ?.let { onUpdate(exercise, it, "weight") }
            },
            label = { Text("Вес (кг)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DistanceInputFields(exercise: Exercise, onUpdate: (Exercise, Int, String) -> Unit) {
    OutlinedTextField(
        value = exercise.plannedDistance?.toString() ?: "",
        onValueChange = { value ->
            value.toIntOrNull()?.let { onUpdate(exercise, it, "distance") }
        },
        label = { Text("Дистанция (метры)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@Composable
fun DurationInputFields(exercise: Exercise, onUpdate: (Exercise, Int, String) -> Unit) {
    OutlinedTextField(
        value = exercise.plannedDuration?.toString() ?: "",
        onValueChange = { value ->
            value.toIntOrNull()?.let { onUpdate(exercise, it, "duration") }
        },
        label = { Text("Длительность (секунды)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@Composable
fun SelfInputFields(exercise: Exercise, onUpdate: (Exercise, Int, String) -> Unit) {
    OutlinedTextField(
        value = exercise.plannedRepetitions?.toString() ?: "",
        onValueChange = { value ->
            value.toIntOrNull()?.let { onUpdate(exercise, it, "repetitions") }
        },
        label = { Text("Повторения") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(1f)
    )
}