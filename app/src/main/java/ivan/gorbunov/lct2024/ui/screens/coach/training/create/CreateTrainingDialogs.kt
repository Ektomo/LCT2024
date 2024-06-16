package ivan.gorbunov.lct2024.ui.screens.coach.training.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ivan.gorbunov.lct2024.gate.data.Exercise

@Composable
fun EditExerciseDialog(
    exercise: Exercise,
    totalExercises: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var newOrder by remember { mutableStateOf(exercise.order.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Редактирование упражнения", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newOrder,
                    onValueChange = { newOrder = it },
                    label = { Text("Новый порядковый номер") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = errorMessage != null,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDelete) {
                        Text("Удалить", color = MaterialTheme.colorScheme.background)
                    }
                    Button(
                        onClick = {
                            val orderInt = newOrder.toIntOrNull()
                            if (orderInt == null || orderInt < 1 || orderInt > totalExercises) {
                                errorMessage = "Введите номер от 1 до $totalExercises"
                            } else {
                                onConfirm(orderInt)
                            }
                        }
                    ) {
                        Text("Сохранить", color = MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    }
}