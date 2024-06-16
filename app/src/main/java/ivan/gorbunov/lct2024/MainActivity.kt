package ivan.gorbunov.lct2024

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ivan.gorbunov.lct2024.ui.screens.auth.login.Role
import ivan.gorbunov.lct2024.ui.screens.graph.MainNavGraph
import ivan.gorbunov.lct2024.ui.screens.graph.UiBottomBar
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import ivan.gorbunov.lct2024.ui.screens.graph.UserBottomNavigation
import ivan.gorbunov.lct2024.ui.theme.Lct2024Theme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var dataStore: LctDataStore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val navController = rememberNavController()

            val isLogin by dataStore.isLogged.collectAsState(false)
            val role by dataStore.role.collectAsState(initial = null)

            val startDestination = if (isLogin) {
                when (role) {
                    Role.Client -> "client"
                    Role.Coach -> "coach"
                    else -> "login"
                }
            } else {
                "login"
            }

            val uiSettings = remember {
                mutableStateOf(UiSettings())
            }

            val bottomBar = remember {
                mutableStateOf(UiBottomBar())
            }

            Lct2024Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        bottomBar.value.bottomBar?.invoke()
                                },
                    topBar = { uiSettings.value.topAppBarContent?.invoke() },
                    floatingActionButton = {
                        uiSettings.value.fabContent?.invoke()
                    }
                ) { innerPadding ->
                    MainNavGraph(
                        navController = navController,
                        paddingValues = innerPadding,
                        uiSettings,
                        bottomBar
                    )
                }
            }

        }
    }


}

