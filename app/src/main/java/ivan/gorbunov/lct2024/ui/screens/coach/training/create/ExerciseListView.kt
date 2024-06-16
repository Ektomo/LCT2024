package ivan.gorbunov.lct2024.ui.screens.coach.training.create

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material.Button
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.ui.screens.client.progress.ChipParameter
import ivan.gorbunov.lct2024.ui.screens.client.progress.FilterChips
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.WorkoutState
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ExerciseList(
    exercises: List<Exercise>,
    chipParameter: TrainFilterParameter,
    uiSettings: MutableState<UiSettings>,
    onPage: () -> Unit,
    onChipFilterClick: (TrainFilterParameter) -> Unit,
    onFilter: (String, String) -> Unit,
    onAddExercise: (Exercise) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        uiSettings.value = UiSettings()
    }

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                if (index == exercises.lastIndex) {
                    onPage()
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {

        item {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onFilter(chipParameter.name, searchText)
                },
                label = { Text("Поиск упражнений") },
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
        }

        item {
            FilterChips(parameters = filters, selectedParameter = chipParameter) {
                onChipFilterClick(it)
            }
        }


        exercises.forEach { exercise ->
            item {
                ExerciseItem(exercise, onAddExercise = {
                    onAddExercise(exercise)
                })
            }
        }

    }
}

@Composable
fun ExerciseItem(exercise: Exercise, onAddExercise: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onAddExercise() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = exercise.photos.firstOrNull()?.url?.replace("8000", "5000") ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
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
                if (exercise.additionalMuscle.isNotEmpty()) {
                    Text(
                        text = "Дополнительные мышцы: ${exercise.additionalMuscle}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Text(
                    text = "Сложность: ${exercise.difficulty}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Оборудование: ${exercise.equipment.ifEmpty { "Без оборудования" }}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuItem(
    modifier: Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddExerciseDialog(exercise: Exercise, onOk: (Exercise) -> Unit, onCancel: () -> Unit) {
    AlertDialog(onDismissRequest = onCancel, confirmButton = {
        Button(onClick = { onOk(exercise) }) {
            Text(text = "Добавить")
        }
    }, dismissButton = {
        Button(onClick = onCancel) {
            Text(text = "Отмена")
        }
    }, title = {
        Text(
            text = "Добавить упражнение?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }, text = {
        Text(
            text = "Вы уверены, что хотите добавить упражнение ${exercise.name}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    })
}


@Composable
fun AddExerciseDialog(
    exercise: Exercise,
    onDismiss: () -> Unit,
    onConfirm: (WorkoutState) -> Unit
) {
    val currentMeasurements = remember { WorkoutState.entries }
    var selectedType: WorkoutState? by remember {
        mutableStateOf(exercise.type)
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Добавим упражнение?", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Вы уверены, что хотите добавить упражнение \"${exercise.name}\"?",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

//                if (selectedType == null) {

                Text(
                    text = "Тип упражнения:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                WorkoutTypeDropdown(
                    selectedType = selectedType,
                    onTypeSelected = {
                        selectedType = it
                    }
                )

//                }
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(modifier = Modifier.weight(1f), onClick = onDismiss) {
                        Text("Нет", color = MaterialTheme.colorScheme.background)
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (selectedType == null) {
                                errorMessage = "Необходимо выбрать тип упражнения"
                            } else {
                                onConfirm(selectedType!!)
                            }
                        }
                    ) {
                        Text("Да", color = MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    }

//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text(text = "Добавление упражнения")
//        },
//        text = {
//            Column {
//                Text(
//                    text = "Вы уверены, что хотите добавить упражнение ${exercise.name}",
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(bottom = 4.dp)
//                )
//
//                if (currentMeasurements.isEmpty()) {
//                    Text(
//                        text = "Измерения",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
//                    )
//
//                    currentMeasurements.forEachIndexed { index, measurement ->
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            var expanded by remember { mutableStateOf(false) }
//                            var selectedMeasurement by remember { mutableStateOf(measurement) }
//
//                            ExposedDropdownMenuBox(
//                                expanded = expanded,
//                                onExpandedChange = { expanded = !expanded }
//                            ) {
//                                OutlinedTextField(
//                                    value = selectedMeasurement,
//                                    onValueChange = { },
//                                    label = { Text("Измерение ${index + 1}") },
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .menuAnchor(),
//                                    readOnly = true,
//                                    trailingIcon = {
//                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                                    }
//                                )
//                                ExposedDropdownMenu(
//                                    expanded = expanded,
//                                    onDismissRequest = { expanded = false }
//                                ) {
//                                    measurementOptions.forEach { option ->
//                                        DropdownMenuItem(
//                                            text = { Text(option) },
//                                            onClick = {
//                                                selectedMeasurement = option
//                                                currentMeasurements[index] = option
//                                                expanded = false
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//
//                            IconButton(onClick = {
//                                currentMeasurements.removeAt(index)
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Delete,
//                                    contentDescription = "Удалить"
//                                )
//                            }
//                        }
//                    }
//
//                    Button(onClick = {
//                        currentMeasurements.add("")
//                    }, modifier = Modifier.padding(top = 8.dp)) {
//                        Text("Добавить измерение")
//                    }
//                }
//
//                if (errorMessage != null) {
//                    Text(
//                        text = errorMessage!!,
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = {
//                    if (currentMeasurements.any { it.isBlank() }) {
//                        errorMessage = "Введите минимум одно измерение"
//                    } else {
//                        onConfirm(currentMeasurements)
//                    }
//                }
//            ) {
//                Text("Добавить")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Отменить")
//            }
//        }
//    )
}

@Composable
fun WorkoutTypeDropdown(
    selectedType: WorkoutState?,
    onTypeSelected: (WorkoutState) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedType?.displayName ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Тип упражнения") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                     Icon(Icons.Default.KeyboardArrowDown, "")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            WorkoutState.entries.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    },
                    text = {
                        Text(type.displayName)
                    })
            }
        }
    }
}