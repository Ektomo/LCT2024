package ivan.gorbunov.lct2024.ui.screens.graph

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
import ivan.gorbunov.lct2024.ui.screens.auth.login.Role
import ivan.gorbunov.lct2024.ui.screens.client.chat.ChatView
import ivan.gorbunov.lct2024.ui.screens.client.chat.ChatViewModel
import ivan.gorbunov.lct2024.ui.screens.client.home.HomeView
import ivan.gorbunov.lct2024.ui.screens.client.home.HomeViewModel
import ivan.gorbunov.lct2024.ui.screens.client.progress.ProgressView
import ivan.gorbunov.lct2024.ui.screens.client.progress.ProgressViewModel
import ivan.gorbunov.lct2024.ui.screens.coach.clients.ClientsView
import ivan.gorbunov.lct2024.ui.screens.coach.clients.ClientsViewModel
import ivan.gorbunov.lct2024.ui.screens.coach.training.TrainingListView
import ivan.gorbunov.lct2024.ui.screens.coach.training.TrainingListViewModel
import ivan.gorbunov.lct2024.ui.screens.coach.training.create.CreateTrainingView
import ivan.gorbunov.lct2024.ui.screens.coach.training.create.CreateTrainingViewModel


fun NavGraphBuilder.coachNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    uiSettings: MutableState<UiSettings>,
    bottomBar: MutableState<UiBottomBar>
) {
//    composable(CoachBottomMenuItem.Home.screen_route) {
//        val vm = hiltViewModel<HomeViewModel>()
//        HomeView(navController = navController, vm, paddingValues, uiSettings)
//    }
    composable(CoachBottomMenuItem.Workouts.screen_route) {
        val vm = hiltViewModel<TrainingListViewModel>()
        TrainingListView(navController = navController, vm, paddingValues, uiSettings)
    }
    composable(CoachNavItem.CreateWorkouts.screen_route) {
        val vm = hiltViewModel<CreateTrainingViewModel>()
        CreateTrainingView(navController = navController, vm, uiSettings, paddingValues)
    }
    composable(CoachBottomMenuItem.Chat.screen_route) { nbs ->
        val isCoach = nbs.arguments?.getString("isCoach")?.toBooleanStrictOrNull() ?: false
        val vm = hiltViewModel<ChatViewModel>()
        vm.type = Role.Coach
        ChatView(vm, paddingValues, uiSettings, true)
    }
    composable(CoachNavItem.ProgressClient.screen_route) {
        val vm = hiltViewModel<ProgressViewModel>()
        ProgressView(vm, paddingValues, uiSettings, true)
    }
    composable(CoachBottomMenuItem.ClientList.screen_route) {
        bottomBar.value = UiBottomBar(bottomBar = {
            CoachBottomNavigation(navController = navController)
        })
        val vm = hiltViewModel<ClientsViewModel>()
        ClientsView(vm, paddingValues, navController, uiSettings)
    }
}

sealed class CoachBottomMenuItem(var title: String, var icon: Int, var screen_route: String) {

    object Chat :
        CoachBottomMenuItem("Чат", R.drawable.baseline_chat_24, "chat_coach")

    object Workouts :
        CoachBottomMenuItem("Тренировки", R.drawable.baseline_work_24, "workouts")

    object ClientList :
        CoachBottomMenuItem("Клиенты", R.drawable.baseline_accessibility_new_24, "clients")
}

sealed class CoachNavItem(val screen_route: String) {

    object CreateWorkouts :
        CoachNavItem("createworkouts")

    object ProgressClient :
        CoachNavItem("progress_client")

}


@Composable
fun CoachBottomNavigation(navController: NavController) {
    val items = listOf(
        CoachBottomMenuItem.ClientList,
        CoachBottomMenuItem.Chat,
        CoachBottomMenuItem.Workouts,

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