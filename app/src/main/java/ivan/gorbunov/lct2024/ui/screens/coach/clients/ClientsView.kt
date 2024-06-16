package ivan.gorbunov.lct2024.ui.screens.coach.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.ui.screens.coach.training.TrainingListScreen
import ivan.gorbunov.lct2024.ui.screens.graph.CoachBottomMenuItem
import ivan.gorbunov.lct2024.ui.screens.graph.CoachNavItem
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import ivan.gorbunov.lct2024.ui.screens.graph.navigateWithBool

@Composable
fun ClientsView(
    vm: ClientsViewModel,
    pv: PaddingValues,
    navController: NavController,
    uiSettings: MutableState<UiSettings>
) {

    uiSettings.value = UiSettings()
    val uiState by vm.uiState.collectAsState()

    ClientsCrossfadeState(
        uiState = uiState,
        modifier = Modifier
            .fillMaxSize()
            .padding(pv),
        listClientContent = {
            ClientListScreen(clients = it, vm, navController)
        },
        chooseTrainContent = {
             TrainingListScreen(trainings = it) {
                 vm.selectTraining(it)
             }
        },
        setTrainingContent = {
            TrainingAssignmentContent(viewModel = vm){
                vm.back {
                    navController.popBackStack()
                }
            }
        },
        onBack = {
            vm.back {
                navController.popBackStack()
            }
        })

}


@Composable
fun ClientListScreen(clients: List<User>, vm: ClientsViewModel, navController: NavController) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск по имени") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            clients.filter {
                it.name?.lowercase()?.contains(searchQuery.lowercase()) == true || it.surname?.lowercase()
                    ?.contains(searchQuery.lowercase()) == true
            }.forEach { client ->
                item {
                    ClientItem(client = client) { action, user ->
                        when (action) {
                            DropdownClientChoose.ASSIGN -> {vm.setAssignmentTraining()}
                            DropdownClientChoose.PROGRESS -> {
                                navController.navigate(CoachNavItem.ProgressClient.screen_route)
                            }
                            DropdownClientChoose.CHAT -> {
                                navController.navigateWithBool(CoachBottomMenuItem.Chat.screen_route, true)
                            }
                            DropdownClientChoose.HISTORY -> {}
                        }
                    }
                }
            }
        }

    }

}