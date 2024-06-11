package ivan.gorbunov.lct2024.ui.screens.core

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class SuccessList<out T>(val data: List<T>) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

