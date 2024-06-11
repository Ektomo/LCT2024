package ivan.gorbunov.lct2024.ui.screens.client.progress

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ivan.gorbunov.lct2024.gate.data.Parameter

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ParameterChips(parameters: List<Parameter>, selectedParameter: Parameter, onParameterSelected: (Parameter) -> Unit) {
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