package ivan.gorbunov.lct2024.ui.screens.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.LctDataStore
import ivan.gorbunov.lct2024.gate.connection.ApiService
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import ivan.gorbunov.lct2024.ui.screens.core.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class LoginData(
    val pass: String,
    val name: String
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: LctDataStore,
    private val apiService: ApiService
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

    fun setInit(){
        setSuccess(Unit)
    }


    fun login(onSuccess: (Role) -> Unit) {
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                apiService.login(_loginData.value.name, _loginData.value.pass)
                apiService.login("client@example.com", "password123")
//                apiService.login("trainer@example.com", "password123")
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

enum class Role{
    Client, Coach
}