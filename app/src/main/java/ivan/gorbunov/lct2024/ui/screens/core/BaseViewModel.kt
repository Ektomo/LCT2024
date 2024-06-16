package ivan.gorbunov.lct2024.ui.screens.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)
    val uiState: StateFlow<UiState<T>> = _uiState
    private val _uiSettings = MutableStateFlow(UiSettings())
    val uiSettings: StateFlow<UiSettings> = _uiSettings

    fun setUiSettings(uiSettings: UiSettings){
        _uiSettings.value = uiSettings
    }
    protected fun setLoading() {
        _uiState.value = UiState.Loading
    }

    protected fun setSuccess(data: T) {
        _uiState.value = UiState.Success(data)
    }

    protected fun setSuccessList(list: List<T>) {
        _uiState.value = UiState.SuccessList(list)
    }

    protected fun setError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    protected fun getList(): List<T>? {
        return if (_uiState.value is UiState.SuccessList<T>){
            (_uiState.value as UiState.SuccessList).data
        }else{
            null
        }
    }

    protected fun launchDataLoad(block: suspend () -> T) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            try {
                val result = block()
                setSuccess(result)
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            }
        }
    }
}