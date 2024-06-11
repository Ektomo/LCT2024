package ivan.gorbunov.lct2024.ui.screens.core

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> CrossfadeState(
    uiState: UiState<T>,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { DefaultErrorContent(it) },
    successListContent: @Composable (List<T>) -> Unit = {},
    successItemContent: @Composable (T) -> Unit = {},
    onBack: (() -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(targetState = uiState, label = "uistate") { state ->
            when (state) {
                is UiState.Loading -> loadingContent()
                is UiState.Success -> successItemContent(state.data)
                is UiState.Error -> errorContent(state.message)
                is UiState.SuccessList -> successListContent(state.data)
            }
        }

        onBack?.let {
            BackHandler(onBack = it)
        }
    }

}


@Composable
fun DefaultLoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun DefaultErrorContent(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}