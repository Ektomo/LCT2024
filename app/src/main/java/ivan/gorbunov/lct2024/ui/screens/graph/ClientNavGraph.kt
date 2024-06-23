package ivan.gorbunov.lct2024.ui.screens.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import ivan.gorbunov.lct2024.R
import ivan.gorbunov.lct2024.ui.screens.client.chat.ChatView
import ivan.gorbunov.lct2024.ui.screens.client.chat.ChatViewModel
import ivan.gorbunov.lct2024.ui.screens.client.home.HomeView
import ivan.gorbunov.lct2024.ui.screens.client.home.HomeViewModel
import ivan.gorbunov.lct2024.ui.screens.client.profile.ProfileView
import ivan.gorbunov.lct2024.ui.screens.client.profile.ProfileViewModel
import ivan.gorbunov.lct2024.ui.screens.client.progress.ProgressView
import ivan.gorbunov.lct2024.ui.screens.client.progress.ProgressViewModel
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_list.TrainingView
import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_list.TrainingViewModel
import ivan.gorbunov.lct2024.ui.screens.client.workout.WorkoutView
import ivan.gorbunov.lct2024.ui.screens.client.workout.WorkoutViewModel


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.clientNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    uiSettings: MutableState<UiSettings>,
    bottomBar: MutableState<UiBottomBar>
) {

//    composable(ClientBottomMenuItem.Home.screen_route) {
//        val vm = hiltViewModel<HomeViewModel>()
//
//        HomeView(navController = navController, vm, paddingValues, uiSettings)
//    }
    composable(ClientBottomMenuItem.Workouts.screen_route) {
        bottomBar.value = UiBottomBar(bottomBar = {
            UserBottomNavigation(navController = navController)
        })
        val vm = hiltViewModel<WorkoutViewModel>()
        WorkoutView(navController = navController, vm, paddingValues, uiSettings)
    }
    composable(ClientBottomMenuItem.Chat.screen_route) { nbs ->
        val isCoach =  nbs.arguments?.getString("isCoach")?.toBooleanStrictOrNull() ?: false
        val vm = hiltViewModel<ChatViewModel>()
        ChatView(vm, paddingValues, uiSettings, isCoach)
    }
    composable(ClientBottomMenuItem.Progress.screen_route) {
        val vm = hiltViewModel<ProgressViewModel>()
        ProgressView(vm, paddingValues, uiSettings)
    }
    composable(ClientNavItem.Training.screen_route) {
        val vm = hiltViewModel<TrainingViewModel>()
        TrainingView(vm = vm, pv = paddingValues, uiSettings, navController)
    }
    composable(ClientBottomMenuItem.Profile.screen_route){
        val vm = hiltViewModel<ProfileViewModel>()
        ProfileView(
            navController = navController,
            vm = vm,
            pv = paddingValues,
            uiSettings = uiSettings
        )
    }

}

sealed class ClientBottomMenuItem(var title: String, var icon: Int, var screen_route: String) {

    object Home :
        ClientBottomMenuItem("Дом", R.drawable.baseline_home_24, "home")

    object Workouts :
        ClientBottomMenuItem("Тренировки", R.drawable.baseline_work_24, "workouts_client")

    object Chat :
        ClientBottomMenuItem("Чат", R.drawable.baseline_chat_24, "chat")

    object Progress :
        ClientBottomMenuItem("Прогресс", R.drawable.baseline_timeline_24, "progress")

    object Profile:
            ClientBottomMenuItem("Профиль", R.drawable.baseline_home_24, "profile")
}

sealed class ClientNavItem(val screen_route: String) {

    object Training : ClientNavItem("training")

}

data class UiSettings(
    var topAppBarContent: (@Composable () -> Unit)? = null,
    var fabContent: (@Composable () -> Unit)? = null,
)

data class UiBottomBar(
    var bottomBar: (@Composable () -> Unit)? = null
)


@Composable
fun UserBottomNavigation(navController: NavController) {
    val items = listOf(
//        ClientBottomMenuItem.Home,
        ClientBottomMenuItem.Workouts,
        ClientBottomMenuItem.Chat,
        ClientBottomMenuItem.Progress,
        ClientBottomMenuItem.Profile
    )



    NavigationBar(
//        contentColor = Color.White, backgroundColor = MaterialTheme.colors.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                selected = currentRoute == item.screen_route,
                label = {
                    Text(text = item.title, fontSize = 9.sp)
                },
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}