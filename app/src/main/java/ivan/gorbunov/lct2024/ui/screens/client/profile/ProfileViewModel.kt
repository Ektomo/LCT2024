package ivan.gorbunov.lct2024.ui.screens.client.profile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.gate.connection.ApiService
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.gate.data.mockUsers
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val service: ApiService
) : BaseViewModel<User>() {


    init {
        setLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val user = service.getAboutMe()
                setSuccess(user)
            }catch (e: Exception){
                setSuccess(mockUsers.first())
            }
        }

    }


}