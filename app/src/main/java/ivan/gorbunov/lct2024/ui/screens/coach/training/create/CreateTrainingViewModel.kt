package ivan.gorbunov.lct2024.ui.screens.coach.training.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.gate.connection.ApiService
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.ui.screens.client.progress.ChipParameter
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTrainingViewModel @Inject constructor(
    val apiService: ApiService
) : ViewModel() {

    private val _exercisesBase: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exerciseBase: StateFlow<List<Exercise>> = _exercisesBase
    private val _uiState = MutableStateFlow<CreateTrainingState>(CreateTrainingState.Loading)
    val uiState: StateFlow<CreateTrainingState> = _uiState
    private val _uiSettings = MutableStateFlow(UiSettings())
    val uiSettings: StateFlow<UiSettings> = _uiSettings
    private val _chipParameter = MutableStateFlow<TrainFilterParameter>(filters[0])
    val chipParameter: StateFlow<TrainFilterParameter> = _chipParameter
    private var baseExercises: List<Exercise> = listOf()
    private var curTrain = Training(
        id = "",
        name = "",
        date = "",
        description = "",
        warmUp = listOf(),
        exercises = listOf(),
        warmDown = listOf()
    )
    private val _trainName = MutableStateFlow<String>("")
    val trainName: StateFlow<String> = _trainName
    private val _trainDescr = MutableStateFlow<String>("")
    val trainDescr: StateFlow<String> = _trainDescr
    private val _exercises = MutableStateFlow<List<Exercise>>(listOf())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private var page = 1
    private var size = 40

    init {
        setTraining(
            curTrain
        )
    }

    fun updateChipParameter(param: TrainFilterParameter) {
        _chipParameter.value = param
    }

    protected fun setLoading() {
        _uiState.value = CreateTrainingState.Loading
    }

    protected fun setTraining(data: Training) {
        curTrain = data
        _uiState.value = CreateTrainingState.TrainingState(curTrain)
    }

    protected fun setError(message: String) {
        _uiState.value = CreateTrainingState.Error(message)
    }

    fun updateName(name: String) {
        _trainName.value = name

    }

    fun updateDescription(description: String) {
        _trainDescr.value = description
    }

    fun updateExercise(body: (List<Exercise>) -> List<Exercise>) {
        val exercises = body(
            exercises.value
        )
        _exercises.value = exercises
        curTrain = curTrain.copy(exercises = exercises)
        setTraining(
            curTrain
        )
    }

    fun updateExerciseOrder(exercise: Exercise, newOrder: Int) {
        val updatedExercises = _exercises.value.toMutableList().apply {
            remove(exercise)
            add(newOrder - 1, exercise.copy(order = newOrder))
            // Update orders
            forEachIndexed { index, ex -> this[index] = ex.copy(order = index + 1) }
        }
        _exercises.value = updatedExercises
    }

    fun deleteExercise(exercise: Exercise) {
        val updatedExercises = _exercises.value.toMutableList().apply {
            remove(exercise)
            // Update orders
            forEachIndexed { index, ex -> this[index] = ex.copy(order = index + 1) }
        }
        _exercises.value = updatedExercises
    }

    fun updateWarmUp(exercise: List<Exercise>) {
        setTraining(
            ((uiState.value as CreateTrainingState.TrainingState)
                .data).copy(warmUp = exercise)
        )
    }

    fun updateWarmDown(exercise: List<Exercise>) {
        setTraining(
            ((uiState.value as CreateTrainingState.TrainingState)
                .data).copy(warmDown = exercise)
        )
    }

    fun back(onPopup: () -> Unit) {
        if (_uiState.value is CreateTrainingState.TrainingState) {
            onPopup()
        } else {
            setTraining(curTrain)
        }
    }

    fun setAddExercises(
        isReset: Boolean = true,
        nameContains: String? = null,
        muscleContains: String? = null,
        additionalMuscleContains: String? = null,
        exerciseTypeContains: String? = null,
        equipmentContains: String? = null,
        difficultyContains: String? = null
    ) {
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isReset) {
                    page = 1
                }
                val newExercises = apiService.getExercises(
                    page = page,
                    size = size,
                    nameContains = nameContains,
                    muscleContains = muscleContains,
                    additionalMuscleContains = additionalMuscleContains,
                    exerciseTypeContains = exerciseTypeContains,
                    equipmentContains = equipmentContains,
                    difficultyContains = difficultyContains
                )
                if (isReset) {
                    baseExercises = newExercises
                } else {
                    baseExercises += newExercises
                }
                _uiState.value = CreateTrainingState.AddExerciseState(baseExercises)
                page++
            } catch (e: Exception) {
                setError(e.message ?: "Ошибка")
            }
        }
    }
}

class TrainFilterParameter(override val name: String, val dataName: String) : ChipParameter {}

val filters = listOf(
    TrainFilterParameter(name = "Название", "name"),
    TrainFilterParameter(name = "Группа мышц", "muscle"),
    TrainFilterParameter(name = "Оборудование", "equipment"),
    TrainFilterParameter(name = "Уровень", "difficulty"),
)