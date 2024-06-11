package ivan.gorbunov.lct2024.ui.screens.client.training.exercise_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.gate.data.trainingMock
import ivan.gorbunov.lct2024.ui.screens.client.training.TrainingUiState
import ivan.gorbunov.lct2024.ui.screens.core.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrainingViewModel(

) : ViewModel() {

    private val _timer = MutableStateFlow("00:00")
    val timer: StateFlow<String> = _timer
    private val _restTimer = MutableStateFlow("120:00")
    val restTimer: StateFlow<String> = _restTimer
    private var curExercise: Exercise? = null
    private var training: Training? = null
    private val _uiState = MutableStateFlow<TrainingUiState>(TrainingUiState.Loading)
    private var _isStarted = MutableStateFlow(false)
    var isStarted: StateFlow<Boolean> = _isStarted
    val uiState: StateFlow<TrainingUiState> = _uiState
    val completedExercises = MutableStateFlow(setOf<Int>())
    private var curTrainState = TrainState.None
    private var seconds = 0

    private var timerJob: Job? = null
    private var timerRestJob: Job? = null

    init {
        getTraining()
    }


    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            seconds = 0
            while (true) {
                _timer.emit("%02d:%02d".format(seconds / 60, seconds % 60))
                delay(1000)
                seconds++
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun markExerciseCompleted(currentIndex: Int) {
        completedExercises.value += currentIndex
    }


    private fun startRestTimer() {
        timerRestJob?.cancel()
        timerRestJob = viewModelScope.launch {
            var seconds = 120
            while (true) {
                _restTimer.emit("%02d:%02d".format(seconds / 60, seconds % 60))
                delay(1000)
                seconds--
            }
        }
    }

    private fun stopRestTimer() {
        timerRestJob?.cancel()
    }

    fun startTrainee() {
        training?.let { training ->
            training.exercises.firstOrNull()?.let {
                curExercise = it
                setExercise(curExercise!!)
                curTrainState = TrainState.Main
                if (!_isStarted.value) {
                    startTimer()
                }
                _isStarted.value = true
            }
        }
    }

    fun startWarmUp() {
        training?.let { training ->
            training.warmUp.firstOrNull()?.let {
                curExercise = it
                setExercise(curExercise!!)
                startTimer()
                curTrainState = TrainState.WarmUp
                _isStarted.value = true
            }
        }
    }

    fun startWarmDown() {
        training?.let { training ->
            training.warmDown.firstOrNull()?.let {
                curExercise = it
                setExercise(curExercise!!)
                curTrainState = TrainState.WarmDown
            }
        }
    }


    fun finishTrainee() {
        setFinish()
        stopTimer()
    }

    fun getCurIdx(): Int {
        return when (curTrainState) {
            TrainState.WarmUp -> training?.warmUp?.indexOf(curExercise) ?: -1
            TrainState.Main, TrainState.None -> training?.exercises?.indexOf(curExercise) ?: -1
            TrainState.WarmDown -> training?.warmDown?.indexOf(curExercise) ?: -1
        }

    }

    fun getCurTrainingState() = curTrainState

    fun trainCount(): Int {
        return training?.exercises?.size ?: 0
    }

    fun completeExercise() {
        curExercise?.let {
            training?.let { training ->
                val currentIndex = getCurIdx()
                when (curTrainState) {
                    TrainState.WarmUp, TrainState.WarmDown -> {
                        nextExercise()
                    }

                    TrainState.Main -> {
                        markExerciseCompleted(currentIndex)
                        if (currentIndex < training.exercises.size - 1) {
                            val nextExercise = training.exercises[currentIndex + 1]
                            setRest(curExercise!!, nextExercise)
                            startRestTimer()
                        } else {
                            setWarmDown(curExercise!!)
                        }
                    }

                    TrainState.None -> setError("Ошибка выполнения")
                }
            }
        }
    }

    fun goToExerciseWith(id: String) {
        val exercise = training?.exercises?.find { it.id == id }
        if (exercise != null) {
            curExercise = exercise
            setExercise(exercise)
        } else {
            setError("Неизвестное упражнение")
        }
    }

    fun nextExercise() {
        training?.let { training ->
            val currentIndex = getCurIdx()
            when (curTrainState) {
                TrainState.WarmUp -> {
                    if (currentIndex < training.warmUp.size - 1) {
                        curExercise = training.warmUp[currentIndex + 1]
                        setExercise(curExercise!!)
                    } else {
                        setWarmUpEnd()
                    }
                }

                TrainState.Main -> {
                    if (currentIndex < training.exercises.size - 1) {
                        curExercise = training.exercises[currentIndex + 1]
                        setExercise(curExercise!!)
                        stopRestTimer()
                    }
//                    else{
//                        curTrainState = TrainState.WarmDown
//                        curExercise = training.warmDown.firstOrNull()
//                        setWarmDown(curExercise!!)
//                    }
                }

                TrainState.WarmDown -> {
                    if (currentIndex < training.warmDown.size - 1) {
                        curExercise = training.warmDown[currentIndex + 1]
                        setExercise(curExercise!!)
                    } else {
                        finishTrainee()
                    }
                }

                TrainState.None -> setError("Ошибка перехода")
            }

        }
    }

    fun previous(onNeedFinish: () -> Unit , onPopUp: () -> Unit) {
        when(_uiState.value){
            is TrainingUiState.Error -> getTraining()
            is TrainingUiState.ExerciseState,  -> {
                if (_isStarted.value){
                    onNeedFinish()
                }else{
                    setTraining(training!!)
                }
            }
            is TrainingUiState.Rest, is TrainingUiState.WarmDownState, TrainingUiState.WarmUpEndState, TrainingUiState.WarmUpState, TrainingUiState.Finish->{
                if (_isStarted.value){
                    onNeedFinish()
                }
            }
            TrainingUiState.Loading -> {}
            TrainingUiState.TakePhoto -> {
                setFinish()
            }
            is TrainingUiState.TrainingState -> {
                 onPopUp()
            }
        }
    }


    protected fun setLoading() {
        _uiState.value = TrainingUiState.Loading
    }

    fun setExercise(data: Exercise) {
        _uiState.value = TrainingUiState.ExerciseState(data)
    }

    fun setTraining(data: Training) {
        _uiState.value = TrainingUiState.TrainingState(data)
    }

    fun goToList(){
        training?.let {
            setTraining(it)
        }
    }


    protected fun setError(message: String) {
        _uiState.value = TrainingUiState.Error(message)
    }

    protected fun setRest(exercise: Exercise, nextExercise: Exercise) {
        _uiState.value = TrainingUiState.Rest(exercise, nextExercise)
    }

    fun setFinish() {
        _uiState.value = TrainingUiState.Finish
    }

    fun setWarmUp() {
        _uiState.value = TrainingUiState.WarmUpState
    }

    protected fun setWarmDown(exercise: Exercise) {
        _uiState.value = TrainingUiState.WarmDownState(exercise)
    }

    protected fun setWarmUpEnd() {
        _uiState.value = TrainingUiState.WarmUpEndState
    }

    fun setTakePhoto(){
        _uiState.value = TrainingUiState.TakePhoto
    }


    private fun getTraining() {
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                setTraining(trainingMock)
                training = trainingMock
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            }
        }
    }

    fun finishTraining(onSuccess: () -> Unit) {
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                launch(Dispatchers.Main){
                    onSuccess()
                }
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            }
        }
    }

}

enum class TrainState {
    WarmUp, Main, WarmDown, None
}