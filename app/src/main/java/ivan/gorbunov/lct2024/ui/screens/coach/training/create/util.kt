package ivan.gorbunov.lct2024.ui.screens.coach.training.create

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.core.TakePhoto

sealed class CreateTrainingState {
    data object Loading : CreateTrainingState()
    data class AddExerciseState(val exercises: List<Exercise>) : CreateTrainingState()
    data class TrainingState(val data: Training) : CreateTrainingState()
    data class Error(val message: String) : CreateTrainingState()
}

@Composable
fun CreateTrainingCrossfadeState(
    uiState: CreateTrainingState,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { DefaultErrorContent(it) },
    trainingContent: @Composable (Training) -> Unit = {},
    addExerciseContent: @Composable (List<Exercise>) -> Unit = {},
    onBack: (() -> Unit)? = null
) {
    Box(modifier.fillMaxSize()) {
        Crossfade(targetState = uiState, label = "uistate") { state ->
            when (state) {
                is CreateTrainingState.TrainingState -> trainingContent(state.data)
                is CreateTrainingState.Loading -> loadingContent()
                is CreateTrainingState.AddExerciseState -> addExerciseContent(state.exercises)
                is CreateTrainingState.Error -> errorContent(state.message)
            }
        }

        onBack?.let {
            BackHandler(onBack = it)
        }
    }
}