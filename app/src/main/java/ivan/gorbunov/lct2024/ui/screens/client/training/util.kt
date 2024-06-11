package ivan.gorbunov.lct2024.ui.screens.client.training

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

sealed class TrainingUiState {
    data object Loading : TrainingUiState()
    data class ExerciseState(val data: Exercise) : TrainingUiState()
    data object WarmUpState : TrainingUiState()
    data object WarmUpEndState : TrainingUiState()
    data class WarmDownState(val lastExercise: Exercise) : TrainingUiState()
    data class TrainingState(val data: Training) : TrainingUiState()
    data class Rest(val exercise: Exercise, val nextExercise: Exercise) : TrainingUiState()
    data object Finish : TrainingUiState()
    data object TakePhoto : TrainingUiState()
    data class Error(val message: String) : TrainingUiState()
}

@Composable
fun TrainingCrossfadeState(
    uiState: TrainingUiState,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { DefaultErrorContent(it) },
    trainingContent: @Composable (Training) -> Unit = {},
    exerciseContent: @Composable (Exercise) -> Unit = {},
    restContent: @Composable (Exercise, Exercise) -> Unit,
    warmUpContent: @Composable () -> Unit = {},
    warmDownContent: @Composable (Exercise) -> Unit = {},
    warmUpEndContent: @Composable () -> Unit = {},
    finishContent: @Composable () -> Unit = {},
    takePhoto: @Composable () -> Unit,
    onBack: (() -> Unit)? = null
) {
    Box(modifier.fillMaxSize()) {
        Crossfade(targetState = uiState, label = "uistate") { state ->
            when (state) {
                is TrainingUiState.TrainingState -> trainingContent(state.data)
                is TrainingUiState.Loading -> loadingContent()
                is TrainingUiState.ExerciseState -> exerciseContent(state.data)
                is TrainingUiState.Error -> errorContent(state.message)
                is TrainingUiState.Rest -> restContent(state.exercise, state.nextExercise)
                is TrainingUiState.WarmDownState -> warmDownContent(state.lastExercise)
                TrainingUiState.Finish -> finishContent()
                TrainingUiState.WarmUpState -> warmUpContent()
                TrainingUiState.WarmUpEndState -> warmUpEndContent()
                TrainingUiState.TakePhoto -> takePhoto()
            }
        }

        onBack?.let {
            BackHandler(onBack = it)
        }
    }
}