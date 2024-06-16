package ivan.gorbunov.lct2024.ui.screens.coach.clients

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.Training
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.core.TakePhoto

sealed class ClientsUiState {
    data object Loading : ClientsUiState()

    data class  ClientsListState(val clients: List<User>): ClientsUiState()
    data object SetTrainingState : ClientsUiState()

    data class ChooseTraining(val list: List<Training>): ClientsUiState()
    data class Error(val message: String) : ClientsUiState()
}

@Composable
fun ClientsCrossfadeState(
    uiState: ClientsUiState,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { DefaultErrorContent(it) },
    listClientContent: @Composable (List<User>) -> Unit = {},
    chooseTrainContent: @Composable (List<Training>) -> Unit = {},
    setTrainingContent: @Composable () -> Unit = {},

    onBack: (() -> Unit)? = null
) {
    Box(modifier.fillMaxSize()) {
        Crossfade(targetState = uiState, label = "uistate") { state ->
            when (state) {
                is ClientsUiState.ClientsListState -> listClientContent(state.clients)
                is ClientsUiState.Loading -> loadingContent()
                is ClientsUiState.Error -> errorContent(state.message)
                is ClientsUiState.ChooseTraining -> chooseTrainContent(state.list)
                is ClientsUiState.SetTrainingState ->  setTrainingContent()
            }
        }

        onBack?.let {
            BackHandler(onBack = it)
        }
    }
}


