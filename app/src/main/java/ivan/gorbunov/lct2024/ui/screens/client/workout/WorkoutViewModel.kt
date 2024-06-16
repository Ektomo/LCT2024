package ivan.gorbunov.lct2024.ui.screens.client.workout

import ivan.gorbunov.lct2024.gate.data.HomeItem
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.gate.data.WorkoutItem
import ivan.gorbunov.lct2024.gate.data.exampleWorkouts
import ivan.gorbunov.lct2024.gate.data.mockTrainings
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import javax.inject.Inject

class WorkoutViewModel @Inject constructor(

) : BaseViewModel<List<Training>>() {

    init {
        setSuccess(mockTrainings)
    }


}