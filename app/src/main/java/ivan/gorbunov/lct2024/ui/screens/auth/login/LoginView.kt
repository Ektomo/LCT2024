package ivan.gorbunov.lct2024.ui.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ivan.gorbunov.lct2024.R
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.core.UiState

@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel,
    paddingValues: PaddingValues
) {

    val uiState by vm.uiState.collectAsState()
    val loginData by vm.loginData.collectAsState()
    val context = LocalContext.current

    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successItemContent = {
            LoginContent(
                loginData = loginData,
                onEmailChange = { vm.updateEmail(it) },
                onPasswordChange = { vm.updatePassword(it) },
                onLogin = {
                    vm.login {
                        if (it == Role.Client) {
                            navController.navigate("client") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate("coach") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                onRegister = {
                    navController.navigate("register") {
                        launchSingleTop = true
                    }
                })
        },
        onBack =
        if (uiState is UiState.Success) {
            null
        } else {
            { vm.setInit() }
        }

    )
}

@Composable
fun LoginContent(
    loginData: LoginData,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {

    val passwordFocusRequester = FocusRequester()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Fitness Logo",
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Добро пожаловать",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = loginData.name,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequester.requestFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = loginData.pass,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onLogin()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Войти")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRegister) {
            Text(text = "Зарегистрироваться", color = MaterialTheme.colorScheme.primary)
        }
    }
}