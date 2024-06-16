package ivan.gorbunov.lct2024.ui.screens.client.progress

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ivan.gorbunov.lct2024.gate.data.Parameter

interface ChipParameter{
    val name: String
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T: ChipParameter> FilterChips(parameters: List<T>, selectedParameter: T, onParameterSelected: (T) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        parameters.forEachIndexed { idx, parameter ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 2.dp),
                selected = selectedParameter == parameter,
                onClick = {
                    onParameterSelected(parameter)
                },
                label = { Text(parameter.name) }
            )
        }
    }
}