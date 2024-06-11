package ivan.gorbunov.lct2024.ui.screens.auth.register

import androidx.lifecycle.viewModelScope
import ivan.gorbunov.lct2024.ui.screens.auth.login.LoginData
import ivan.gorbunov.lct2024.ui.screens.auth.login.Role
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    // Внедрите необходимые зависимости, например, репозиторий для анкеты
) : BaseViewModel<Unit>(){

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

    fun register(onSuccess: (Role) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            try {
                delay(2000)
                onSuccess(Role.Client)
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            }
        }
    }

}