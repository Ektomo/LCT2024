package ivan.gorbunov.lct2024.ui.screens.client.home

import dagger.hilt.android.lifecycle.HiltViewModel
import ivan.gorbunov.lct2024.gate.data.HomeCategories
import ivan.gorbunov.lct2024.gate.data.HomeItem
import ivan.gorbunov.lct2024.gate.data.homeItems

import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : BaseViewModel<HomeItem>() {

    init {
        setLoading()
        setSuccessList(homeItems)
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