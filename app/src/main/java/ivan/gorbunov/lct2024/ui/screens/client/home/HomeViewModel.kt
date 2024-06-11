package ivan.gorbunov.lct2024.ui.screens.client.home

import ivan.gorbunov.lct2024.gate.data.HomeCategories
import ivan.gorbunov.lct2024.gate.data.HomeItem
import ivan.gorbunov.lct2024.gate.data.mockGateNews
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(

) : BaseViewModel<HomeItem>() {

    init {
        setSuccessList(mockGateNews)
    }

    fun goToCard(item: HomeItem){
        when(item.cat){
            HomeCategories.News -> {

            }
            HomeCategories.Coaches -> {

            }
            HomeCategories.Workouts -> {

            }
            HomeCategories.Receipts -> {

            }
        }
    }



}