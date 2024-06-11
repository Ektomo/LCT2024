package ivan.gorbunov.lct2024.ui.screens.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ivan.gorbunov.lct2024.ui.screens.auth.login.LoginScreen
import ivan.gorbunov.lct2024.ui.screens.auth.login.LoginViewModel
import ivan.gorbunov.lct2024.ui.screens.auth.register.RegisterView
import ivan.gorbunov.lct2024.ui.screens.auth.register.RegisterViewModel


@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
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
        composable("client") {
            val clientNavController = rememberNavController()
            val uiSettings = remember {
                mutableStateOf(UiSettings())
            }
            Scaffold(
                bottomBar = { UserBottomNavigation(navController = clientNavController) },
                topBar = { uiSettings.value.topAppBarContent?.invoke() },
                floatingActionButton = { uiSettings.value.fabContent?.invoke() }
            ) { pv ->
                ClientNavGraph(clientNavController, pv, uiSettings)
            }

        }
        composable("coach") {
            val clientNavController = rememberNavController()
            val uiSettings = remember {
                mutableStateOf(UiSettings())
            }
            Scaffold(
                bottomBar = { CoachBottomNavigation(navController = clientNavController) },
                topBar = { uiSettings.value.topAppBarContent?.invoke() },
                floatingActionButton = { uiSettings.value.fabContent?.invoke() }
            ) { pv ->
                CoachNavGraph(clientNavController, pv, uiSettings)
            }

        }
    }
}