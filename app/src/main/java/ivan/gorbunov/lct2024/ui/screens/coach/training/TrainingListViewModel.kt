package ivan.gorbunov.lct2024.ui.screens.coach.training

import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.gate.data.mockTrainings
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import ivan.gorbunov.lct2024.ui.screens.core.UiState
import javax.inject.Inject

class TrainingListViewModel @Inject constructor(

) : BaseViewModel<Training>() {

    var trainings = listOf<Training>()
    var curTraining: Training? = null



    init {
        trainings = mockTrainings
        setSuccessList(mockTrainings)
    }

    fun goToTrainWith(id: String) {
        val training = trainings.find { it.id == id }
        if (training != null) {
            curTraining = training
            setSuccess(curTraining!!)
        } else {
            setError("Неизвестное упражнение")
        }
    }

    fun back(goBack: () -> Unit){
        if (uiState.value is UiState.Success){
            setSuccessList(trainings)
        }else{
            goBack()
        }
    }

}