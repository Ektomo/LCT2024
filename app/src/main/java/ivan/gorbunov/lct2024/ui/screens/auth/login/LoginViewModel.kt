package ivan.gorbunov.lct2024.ui.screens.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.LctDataStore
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import ivan.gorbunov.lct2024.ui.screens.core.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginData(
    val pass: String,
    val name: String
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: LctDataStore,
//    val gate: LctGate
) : BaseViewModel<Unit>() {


    private val _loginData = MutableStateFlow(LoginData("", ""))
    val loginData: StateFlow<LoginData> = _loginData.asStateFlow()

    fun updateEmail(newEmail: String) {
        _loginData.value = _loginData.value.copy(name = newEmail)
    }

    fun updatePassword(newPassword: String) {
        _loginData.value = _loginData.value.copy(pass = newPassword)
    }

    init {
        setSuccess(Unit)
    }


    fun login(context: Context, onSuccess: (Role) -> Unit) {
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {

            try {
//                delay(2000)
                dataStore.setRole(Role.Coach)
                dataStore.setIsLogged(true)
                launch(Dispatchers.Main){
                    onSuccess(Role.Coach)
                }
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            }
        }
    }

}

enum class Role{
    Client, Coach
}