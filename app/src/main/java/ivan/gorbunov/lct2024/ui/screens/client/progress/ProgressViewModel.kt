package ivan.gorbunov.lct2024.ui.screens.client.progress

import ivan.gorbunov.lct2024.gate.data.ChartData
import ivan.gorbunov.lct2024.gate.data.Parameter
import ivan.gorbunov.lct2024.gate.data.WorkoutItem
import ivan.gorbunov.lct2024.gate.data.runData
import ivan.gorbunov.lct2024.gate.data.strengthData
import ivan.gorbunov.lct2024.gate.data.weightData
import ivan.gorbunov.lct2024.ui.screens.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProgressViewModel (

) : BaseViewModel<ChartData>() {

    private val _selectedChip = MutableStateFlow(Parameter(""))
    val selectedChip: StateFlow<Parameter> = _selectedChip

    init {
        setSuccessList(weightData)
    }

    fun updateFilterChip(parameter: Parameter){
        val data = when(parameter.name){
            "Вес" -> weightData
            "Силовые показатели" -> strengthData
            "Беговые показатели" -> runData
            else -> weightData
        }
        setSuccessList(data)
        _selectedChip.value = parameter
    }

}