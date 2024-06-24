package ivan.gorbunov.lct2024.ui.screens.client.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ivan.gorbunov.lct2024.gate.data.HomeCategories
import ivan.gorbunov.lct2024.gate.data.HomeItem
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavHostController,
    vm: HomeViewModel,
    pv: PaddingValues,
    uiSettings: MutableState<UiSettings>
) {

    val uiState by vm.uiState.collectAsState()
    uiSettings.value = UiSettings()

    var selectedItem by remember { mutableStateOf<HomeItem?>(null) }
    var scope = rememberCoroutineScope()


    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successListContent = { list ->
            val bottomSheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Hidden,
                skipHiddenState = false
            )
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
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.End
//                        ) {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    bottomSheetState.hide()
//                                }
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Close,
//                                    contentDescription = "Close"
//                                )
//                            }
//                        }

                        selectedItem?.img?.let {
                            Image(
                                painterResource(id = it),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.FillHeight
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = selectedItem?.title ?: "Наименование",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

//                        selectedItem?.data?.get("date")?.let {
//                            Text(
//                                text = it,
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color.Gray,
//                                textAlign = TextAlign.Center,
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        selectedItem?.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (selectedItem?.cat == HomeCategories.Coaches) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {  }, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Выбрать",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }else if (selectedItem?.cat == HomeCategories.Receipts){
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {  }, modifier = Modifier.fillMaxWidth()) {

                                Text(
                                    text = "Поделиться",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                sheetPeekHeight = 0.dp
            ) {
                HomeContent(homeData = list, pv) {
                    selectedItem = it
                    scope.launch {
                        bottomSheetState.expand()
                    }
                }
            }
        }
    )

}

@Composable
fun HomeContent(
    homeData: List<HomeItem>,
    pv: PaddingValues,
    onItemClick: (HomeItem) -> Unit,
) {

    val scrollState = rememberScrollState()


    val newsItems = homeData.filter { it.cat == HomeCategories.News }
    val coachesItems = homeData.filter { it.cat == HomeCategories.Coaches }
//    val workoutsItems = homeData.filter { it.cat == HomeCategories.Workouts }
    val receipts = homeData.filter { it.cat == HomeCategories.Receipts }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues = pv)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "Новости",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            newsItems.forEach { newsItem ->
                ItemCard(newsItem, onItemClick)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }



        Spacer(modifier = Modifier.height(32.dp))


        Text(
            text = "Наши тренеры",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            coachesItems.forEach { coach ->
                ItemCard(coach, onItemClick)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

//        Spacer(modifier = Modifier.height(32.dp))
//
//        Text(
//            text = "Самостоятельные тренировки",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//            workoutsItems.forEach { workout ->
//                ItemCard(workout, onItemClick)
//                Spacer(modifier = Modifier.width(4.dp))
//            }
//        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Рецепты",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            receipts.forEach { receipt ->
                ItemCard(receipt, onItemClick)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun ItemCard(item: HomeItem, onItemClick: (HomeItem) -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .background(color = NavigationBarDefaults.containerColor)
//        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
    ) {
        Column(modifier = Modifier
            .width(300.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .clickable { onItemClick(item) }) {
            Image(
                painterResource(id = item.img!!),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.title ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
//                item.data["description"]?.let {
//                    Text(
//                        text = it,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DetailBottomSheet(item: HomeItem, onDismiss: () -> Unit) {
//
//    // Empty content for the main scaffold, as the bottom sheet will cover the screen
//}
//}