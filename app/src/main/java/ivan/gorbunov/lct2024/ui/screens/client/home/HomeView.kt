package ivan.gorbunov.lct2024.ui.screens.client.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ivan.gorbunov.lct2024.gate.data.HomeCategories
import ivan.gorbunov.lct2024.gate.data.HomeItem
import ivan.gorbunov.lct2024.ui.screens.core.CrossfadeState
import ivan.gorbunov.lct2024.ui.screens.core.DefaultErrorContent
import ivan.gorbunov.lct2024.ui.screens.core.DefaultLoadingContent
import ivan.gorbunov.lct2024.ui.screens.graph.UiSettings

@Composable
fun HomeView(navController: NavHostController, vm: HomeViewModel, pv: PaddingValues, uiSettings: MutableState<UiSettings>){

    val uiState by vm.uiState.collectAsState()
    uiSettings.value = UiSettings()


    CrossfadeState(
        uiState = uiState,
        loadingContent = { DefaultLoadingContent() },
        errorContent = { message -> DefaultErrorContent(message) },
        successListContent = { list ->
            HomeContent(homeData = list, pv) {
                vm.goToCard(it)
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
    val workoutsItems = homeData.filter { it.cat == HomeCategories.Workouts }
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
            text = "Наши тренера",
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

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Самостоятельные тренировки",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            workoutsItems.forEach { workout ->
                ItemCard(workout, onItemClick)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

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
        modifier = Modifier.padding(bottom = 8.dp).background(color = NavigationBarDefaults.containerColor)
//        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
    ) {
        Column(modifier = Modifier
            .width(300.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .clickable { onItemClick(item) }) {
            AsyncImage(
                model = item.img,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.data["title"] ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                item.data["description"]?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}