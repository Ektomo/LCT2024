package ivan.gorbunov.lct2024.ui.screens.coach.training.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.ui.screens.client.progress.ChipParameter
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings

@Composable
fun CreateTrainingView(
    navController: NavController,
    viewModel: CreateTrainingViewModel,
    uiSettings: MutableState<UiSettings>,
    pv: PaddingValues
) {

    val uiState by viewModel.uiState.collectAsState()
    val exerciseBase by viewModel.exerciseBase.collectAsState()
    val chipParameter by viewModel.chipParameter.collectAsState()
    var chooseExercise: Exercise? by remember {
        mutableStateOf(null)
    }
    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var onBackValue by remember {
        mutableIntStateOf(1)
    }

    var curFilter by remember {
        mutableStateOf("")
    }

    val measurementOptions = listOf("count", "weight", "distance", "duration")

    chooseExercise?.let { exercise ->
        if (showAddDialog) {
            AddExerciseDialog(
                exercise = exercise,
                onConfirm = { st ->

                    viewModel.updateExercise {
                        it + exercise.copy(order = it.size + 1, type = st)
                    }
                    chooseExercise = null
                    showAddDialog = false
                    onBackValue += 1
                },
                onDismiss = {
                    chooseExercise = null
                    showAddDialog = false
                })
        }
    }

    LaunchedEffect(key1 = onBackValue) {
        uiSettings.value = UiSettings(
            fabContent = {
                FloatingActionButton(onClick = {
//                    viewModel.saveTraining()
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Сохранить")
                }
            }
        )
    }

    CreateTrainingCrossfadeState(
        uiState = uiState,
        modifier = Modifier.padding(pv),
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        trainingContent = { training ->
            CreateTrainingContent(
                vm = viewModel,
                training = training,
                onName = {
                    viewModel.updateName(it)
                }, onDescription = {
                    viewModel.updateDescription(it)
                },
                onAddExerciseBtn = {
                    viewModel.setAddExercises()
                })
        },
        addExerciseContent = { list ->
            ExerciseList(
                chipParameter = chipParameter,
                exercises = list,
                uiSettings = uiSettings,
                onChipFilterClick = { chip ->
                    viewModel.updateChipParameter(chip)
                },
                onPage = {
                    viewModel.setAddExercises(false)
                },
                onFilter = { chips, value ->
                    var needChange = false
                    if (curFilter != chips){
                        curFilter = chips
                        needChange = true
                    }
                    when(chips){
                        "name" -> {
                            viewModel.setAddExercises(
                                isReset = needChange,
                                nameContains = value
                            )
                        }
                        "muscle" -> {
                            viewModel.setAddExercises(
                                isReset = needChange,
                                muscleContains = value
                            )
                        }
                        "equipment" -> {
                            viewModel.setAddExercises(
                                isReset = needChange,
                                equipmentContains = value
                            )
                        }
                        "difficulty" -> {
                            viewModel.setAddExercises(
                                isReset = needChange,
                                difficultyContains = value
                            )
                        }
                    }
                }, onAddExercise = {
                    chooseExercise = it
                    showAddDialog = true
                })
        },
        onBack = {
            onBackValue += 1
            if (showAddDialog) {
                chooseExercise = null
                showAddDialog = false
            } else {
                viewModel.back {
                    navController.popBackStack()
                }
            }
        }
    )


}

@Composable
fun CreateTrainingContent(
    vm: CreateTrainingViewModel,
    training: Training,
    onAddExerciseBtn: () -> Unit,
    onName: (String) -> Unit,
    onDescription: (String) -> Unit
) {

    val name by vm.trainName.collectAsState()
    val descr by vm.trainDescr.collectAsState()
    val exercises by vm.exercises.collectAsState()
    var showDialog by remember {
        mutableStateOf(false)
    }
    var choseForEdit: Exercise? by remember {
        mutableStateOf(null)
    }

    if (showDialog && choseForEdit != null) {
        EditExerciseDialog(
            exercise = choseForEdit!!,
            totalExercises = exercises.size,
            onDismiss = {
                showDialog = false
                choseForEdit = null
            },
            onConfirm = {
                vm.updateExerciseOrder(choseForEdit!!, it)
                showDialog = false
                choseForEdit = null
            },
            onDelete = {
                vm.deleteExercise(choseForEdit!!)
                showDialog = false
                choseForEdit = null
            }
        )
    }


    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { onName(it) },
            label = { Text("Название тренировки") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = descr,
            onValueChange = { onDescription(it) },
            label = { Text("Описание") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextButton(onClick = onAddExerciseBtn) {
            Text(
                text = "Добавить упражнение",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
        }
        ExerciseList(
            exercises = exercises,
            onEditExercise = {
                showDialog = true
                choseForEdit = it
            }
        )
    }
}

@Composable
fun ExerciseList(
    exercises: List<Exercise>,
    onEditExercise: (Exercise) -> Unit
) {
    LazyColumn {
        exercises.forEach { exercise ->
            item {
                ExerciseItem(exercise, onEditExercise)
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, onEditExercise: (Exercise) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onEditExercise(exercise) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Row {
                AsyncImage(
                    model = exercise.photos.firstOrNull()?.url ?: "",
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
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
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Оборудование: ${exercise.equipment.ifEmpty { "Без оборудования" }}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Text(
                text = exercise.order.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
        }
    }
}
