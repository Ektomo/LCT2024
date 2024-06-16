package ivan.gorbunov.lct2024.ui.screens.coach.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.gate.connection.ApiService
import ivan.gorbunov.lct2024.gate.data.ChartData
import ivan.gorbunov.lct2024.gate.data.Parameter
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.gate.data.mockTrainings
import ivan.gorbunov.lct2024.gate.data.mockUsers
import ivan.gorbunov.lct2024.gate.data.runData
import ivan.gorbunov.lct2024.gate.data.strengthData
import ivan.gorbunov.lct2024.gate.data.weightData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientsUiState>(ClientsUiState.Loading)
    val uiState: StateFlow<ClientsUiState> = _uiState
    private val _selectedTraining = MutableStateFlow<Training?>(null)
    val selectedTraining: StateFlow<Training?> = _selectedTraining

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate

    private val _roundCount = MutableStateFlow<Int?>(3)
    val roundCount: StateFlow<Int?> = _roundCount

    private val _selectedChip = MutableStateFlow(Parameter(""))
    val selectedChip: StateFlow<Parameter> = _selectedChip

    init {
        setClients()
    }

    fun setClients() {
        _uiState.value = ClientsUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val clients = apiService.getClients()
                _uiState.value = ClientsUiState.ClientsListState(clients)
            } catch (e: Exception) {
                _uiState.value = ClientsUiState.Error(e.message ?: "Ошибка")
            }


        }

    }

    fun back(onBack: () -> Unit) {
        if (_uiState.value is ClientsUiState.ClientsListState) {
            onBack()
        } else {
            if (_uiState.value is ClientsUiState.ChooseTraining) {
                setAssignmentTraining()
            } else {
                setClients()
            }
        }
    }

    fun selectTraining(training: Training) {
        _selectedTraining.value = training
        setAssignmentTraining()
    }

    fun setAssignmentTraining() {
        _uiState.value = ClientsUiState.SetTrainingState
    }

    fun setSelectTrain(training: List<Training> = mockTrainings) {
        _uiState.value = ClientsUiState.ChooseTraining(training)
    }

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun updateRoundCount(roundCount: Int?) {
        _roundCount.value = roundCount
    }

    fun updateExerciseValue(exerciseId: Int, value: Int, field: String) {
        _selectedTraining.value = _selectedTraining.value?.let { training ->
            training.copy(exercises = training.exercises.map {
                if (it.id == exerciseId) {
                    when (field) {
                        "repetitions" -> it.copy(plannedRepetitions = value)
                        "weight" -> it.copy(plannedWeight = value)
                        "distance" -> it.copy(plannedDistance = value)
                        "duration" -> it.copy(plannedDuration = value)
                        "roundCount" -> it.copy(roundCount = value)
                        else -> it
                    }
                } else it
            })
        }
    }

//    fun updateFilterChip(parameter: Parameter){
//        val data = when(parameter.name){
//            "Вес" -> weightData
//            "Силовые показатели" -> strengthData
//            "Беговые показатели" -> runData
//            else -> weightData
//        }
//        setSuccessList(data)
//        _selectedChip.value = parameter
//    }
//
//    fun setProgress(data: List<ChartData>){
//
//    }

}