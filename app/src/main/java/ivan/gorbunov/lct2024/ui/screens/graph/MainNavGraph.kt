package ivan.gorbunov.lct2024.ui.screens.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ivan.gorbunov.lct2024.ui.screens.auth.login.LoginScreen
import ivan.gorbunov.lct2024.ui.screens.auth.login.LoginViewModel
import ivan.gorbunov.lct2024.ui.screens.auth.register.RegisterView
import ivan.gorbunov.lct2024.ui.screens.auth.register.RegisterViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    uiSettings: MutableState<UiSettings>,
    bottomBar: MutableState<UiBottomBar>
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val vm = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, vm, paddingValues)
        }
        composable("register") {
            val vm = hiltViewModel<RegisterViewModel>()
            RegisterView(navController = navController, vm)
        }
        navigation(ClientBottomMenuItem.Workouts.screen_route, "client") {

            clientNavGraph(navController, paddingValues, uiSettings, bottomBar)

        }
        navigation(CoachBottomMenuItem.ClientList.screen_route, "coach") {

            coachNavGraph(navController, paddingValues, uiSettings, bottomBar)
        }
    }
}

fun NavController.navigateWithBool(route: String, isCoach: Boolean) {
    this.navigate("$route?isCoach=${isCoach}")
}