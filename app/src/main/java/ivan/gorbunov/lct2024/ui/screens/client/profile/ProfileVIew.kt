package ivan.gorbunov.lct2024.ui.screens.client.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ivan.gorbunov.lct2024.R
import ivan.gorbunov.lct2024.gate.data.User
import ivan.gorbunov.lct2024.gate.data.mockUsers
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.launch

@Composable
fun ProfileView(
    navController: NavHostController,
    vm: ProfileViewModel,
    pv: PaddingValues,
    uiSettings: MutableState<UiSettings>
) {

    val uiState by vm.uiState.collectAsState()
    uiSettings.value = UiSettings()




    CrossfadeState(
        uiState = uiState,
        modifier = Modifier
            .padding(pv)
            .padding(horizontal = 8.dp),
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successItemContent = { user ->
            ProfileItemContent(user = user)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileItemContent(user: User) {
    val scrollState = rememberScrollState()
    var showShop by remember {
        mutableStateOf(false)
    }
    var showHistory by remember {
        mutableStateOf(false)
    }

    var showAlert by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    if (showAlert){
        FixAlert {
            showAlert = false
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                if (showShop) {
                    Text(
                        text = "Ваш баланс: 1500 монет",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
//                            .background(color = NavigationBarDefaults.containerColor)
                        border = BorderStroke(
                            width = 1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        ),
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.fut),
                                contentDescription = "Merch",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Футболка с логотипом - 1000 монет", fontSize = 16.sp)
                                Button(onClick = { /* Purchase */ }) {
                                    Text("Купить")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
//                            .background(color = NavigationBarDefaults.containerColor)
//        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sale),
                                contentDescription = "Discount",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Скидка на абонемент 15% - 500 монет", fontSize = 16.sp)
                                Button(onClick = { /* Purchase */ }) {
                                    Text("Купить")
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "История событий",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
//                            .background(color = NavigationBarDefaults.containerColor)

                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Достижение", fontSize = 16.sp)
                            Text("Завершил 10 тренировок", fontSize = 14.sp)
                            Text("Дата: 10.02.2024", fontSize = 14.sp)
                        }

                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
//                            .background(color = NavigationBarDefaults.containerColor)

                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Достижение", fontSize = 16.sp)
                            Text("Стал лучшим новичком месяца", fontSize = 14.sp)
                            Text("Дата: 11.02.2024", fontSize = 14.sp)
                        }

                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
//                            .background(color = NavigationBarDefaults.containerColor)

                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Анти достижение", fontSize = 16.sp)
                            Text("Пропустил 3 тренировки подряд", fontSize = 14.sp)
                            Text("Дата: 14.02.2024", fontSize = 14.sp)
                            Button(onClick = {
                                showAlert = true
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Исправить")
                            }
                        }

                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {

        // Empty content for the main scaffold, as the bottom sheet will cover the screen


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Profile Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = user.email ?: "Имя аккаунта",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                DropdownTitules()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {


                    Text("Имя: ${user.name}", fontSize = 16.sp)
                    Text("Фамилия: ${user.surname}", fontSize = 16.sp)
                    Text("Возраст: ${user.age} лет", fontSize = 16.sp)
                    Text("Вес: ${user.weight} кг", fontSize = 16.sp)
                    Text("Рост: ${user.height} см", fontSize = 16.sp)
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Редактировать", fontSize = 16.sp)

                    }
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    var curPerson by remember {
                        mutableFloatStateOf(1f)
                    }

                    when {
                        curPerson >= 1f && curPerson < 2f -> Image(
                            painterResource(
                                id =
                                R.drawable.person1
                            ),
                            modifier = Modifier.height(300.dp),
                            contentDescription = "",
                        )

                        curPerson >= 2f && curPerson < 3f -> Image(
                            painterResource(
                                id =
                                R.drawable.person2
                            ),
                            modifier = Modifier.height(300.dp),
                            contentDescription = "",
                        )

                        curPerson >= 3f && curPerson < 4f -> Image(
                            painterResource(
                                id =
                                R.drawable.person3
                            ),
                            modifier = Modifier.height(300.dp),
                            contentDescription = "",
                        )

                        curPerson >= 4f && curPerson < 5f -> Image(
                            painterResource(
                                id =
                                R.drawable.person4
                            ),
                            modifier = Modifier.height(300.dp),
                            contentDescription = "",
                        )

                        curPerson >= 5f && curPerson < 6f -> Image(
                            painterResource(
                                id =
                                R.drawable.person5
                            ),
                            modifier = Modifier.height(300.dp),
                            contentDescription = "",
                        )

                        curPerson >= 6f -> Image(
                            painterResource(
                                id =
                                R.drawable.person6
                            ),
                            modifier = Modifier.height(300.dp),
                            contentDescription = "",
                        )
                    }


                    Slider(value = curPerson, onValueChange = {
                        curPerson = it
                    }, valueRange = 1f..6f, steps = 4)
                }
            }

            Text("Ваш счет: 1500 монет", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = {
                showShop = true
                showHistory = false
                coroutineScope.launch {
                    bottomSheetState.expand()

                }

            }) {
                Text("Магазин", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Текущие задания", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("1. Завершить 5 тренировок за неделю - 200 монет", fontSize = 16.sp)
            Text("2. Посетить 3 разных тренировки - 300 монет", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                showShop = false
                showHistory = true
                coroutineScope.launch {
                    bottomSheetState.expand()
                }
            }) {
                Text("История достижений", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }


        }
    }

//    if (showShop){
//        ShopBottomSheet {
//            showShop = false
//        }
//    }
//
//    if (showHistory){
//        HistoryBottomSheet {
//            showHistory = false
//        }
//    }

}

@Composable
fun DropdownTitules() {
    var curTitul by remember {
        mutableStateOf(titules.first())
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = curTitul)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            titules.forEach { name ->
                DropdownMenuItem(
                    onClick = {
                        curTitul = name
                        expanded = false
                    },
                    text = {
                        Text(text = name)
                    }
                )
            }
        }
    }
}

val titules = listOf(
    "Новичок",
    "Завоеваетель",
    "Геракл",
    "Прогульщик"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopBottomSheet(onDismiss: () -> Unit) {
    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden)
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.fut),
                        contentDescription = "Merch",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Футболка с логотипом - 1000 монет", fontSize = 16.sp)
                        Button(onClick = { /* Purchase */ }) {
                            Text("Купить")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.sale),
                        contentDescription = "Discount",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Скидка на абонемент 10% - 500 монет", fontSize = 16.sp)
                        Button(onClick = { /* Purchase */ }) {
                            Text("Купить")
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {

        // Empty content for the main scaffold, as the bottom sheet will cover the screen
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryBottomSheet(onDismiss: () -> Unit) {
    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Завершил 10 тренировок(10.02.2024)", fontSize = 16.sp)
                Text("Стал лучшим новичком месяца(11.02.2024)", fontSize = 16.sp)
                Text("Пропустил 3 тренировки подряд(14.02.2024)", fontSize = 16.sp)
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        // Empty content for the main scaffold, as the bottom sheet will cover the screen
    }
}

@Preview
@Composable
fun PreviewUser() {
    ProfileItemContent(user = mockUsers.first())
}

@Composable
fun FixAlert(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "Понятно")
            }
        },
        icon = { Icon(Icons.Default.Info, "") },
        title = {
            Text(
                text = "Инструкция",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = { Text(text = "Для исправления анти достижения необходимо не пропускать назначенные тренировки на протяжении месяца. После выполнения требований антидостижение автоматически снимется") })
}