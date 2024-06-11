package ivan.gorbunov.lct2024

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ivan.gorbunov.lct2024.ui.screens.auth.login.Role
import ivan.gorbunov.lct2024.ui.screens.graph.MainNavGraph
import ivan.gorbunov.lct2024.ui.theme.Lct2024Theme
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var dataStore: LctDataStore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val navController = rememberNavController()

            val isLogin by dataStore.isLogged.collectAsState(false)
            val role by dataStore.role.collectAsState(initial = null)

            val startDestination = if (isLogin){
                when(role){
                    Role.Client -> "client"
                    Role.Coach -> "coach"
                    else -> "login"
                }
            }else{
                "login"
            }

            Lct2024Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavGraph(navController = navController, paddingValues = innerPadding)
                }
            }

        }
    }


}

