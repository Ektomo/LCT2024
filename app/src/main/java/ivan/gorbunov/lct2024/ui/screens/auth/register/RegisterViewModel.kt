package ivan.gorbunov.lct2024.ui.screens.auth.register

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.LctDataStore
import ivan.gorbunov.lct2024.gate.connection.ApiService
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

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val apiService: ApiService,
    val dataStore: LctDataStore
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
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiService.register(_loginData.value.name, _loginData.value.pass)
                val user = apiService.getAboutMe()
                dataStore.setIsLogged(true)
                if (user.role == "client"){
                    dataStore.setRole(Role.Client)
                    launch(Dispatchers.Main){
                        onSuccess(Role.Client)
                    }
                }else {
                    dataStore.setRole(Role.Coach)
                    launch(Dispatchers.Main){
                        onSuccess(Role.Coach)
                    }
                }
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            }
        }
    }

}