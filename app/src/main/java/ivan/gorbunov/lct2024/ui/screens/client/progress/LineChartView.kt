package ivan.gorbunov.lct2024.ui.screens.client.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import ivan.gorbunov.lct2024.gate.data.ChartData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

@Composable
fun LineChartWithAxes(
    data: List<ChartData>,
    modifier: Modifier = Modifier,
    xLabel: String = "Дата",
    yLabel: String = "Значение"
) {
    if (data.isEmpty()) return

    val maxValue = data.maxOf { it.value }
    val minValue = data.minOf { it.value }

    val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    val dates = data.map { dateFormat.parse(it.date)!! }

    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val axisColor = MaterialTheme.colorScheme.onSurface
    val textColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val chartWidth = size.width
        val chartHeight = size.height
        val padding = 16.dp.toPx()
        val xAxisSpace = (chartWidth - padding * 2) / 4
        val yAxisSpace = (chartHeight - padding * 2) / 5

        // Draw background
        drawRect(
            color = backgroundColor,
            size = Size(chartWidth, chartHeight)
        )

        // Draw horizontal grid lines
        for (i in 0..5) {
            val y = padding + i * yAxisSpace
            drawLine(
                color = gridColor,
                start = Offset(padding, y),
                end = Offset(chartWidth - padding, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw vertical grid lines
        for (i in 0..4) {
            val x = padding + i * xAxisSpace
            drawLine(
                color = gridColor,
                start = Offset(x, padding),
                end = Offset(x, chartHeight - padding),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw X and Y axes
        drawLine(
            color = axisColor,
            start = Offset(padding, chartHeight - padding),
            end = Offset(chartWidth - padding, chartHeight - padding),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = axisColor,
            start = Offset(padding, padding),
            end = Offset(padding, chartHeight - padding),
            strokeWidth = 2.dp.toPx()
        )

        // Draw X axis labels
        val interval = (dates.last().time - dates.first().time) / 4
        val dateLabels = List(5) { index ->
            Date(dates.first().time + interval * index)
        }

        dateLabels.forEachIndexed { index, date ->
            val x = padding + index * xAxisSpace
            drawContext.canvas.nativeCanvas.drawText(
                dateFormat.format(date),
                x,
                chartHeight,
                android.graphics.Paint().apply {
                    color = textColor.toArgb()
                    textSize = 12.sp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }

        // Draw Y axis labels
        for (i in 0..5) {
            val value = round((minValue + i * (maxValue - minValue) / 5) * 10) / 10
            val y = chartHeight - padding - i * yAxisSpace
            drawContext.canvas.nativeCanvas.drawText(
                value.toString(),
                padding - 10.dp.toPx(),
                y,
                android.graphics.Paint().apply {
                    color = textColor.toArgb()
                    textSize = 12.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
            )
        }

        // Draw the line graph
        val path = Path().apply {
            val points = data.mapIndexed { index, chartData ->
                Offset(
                    x = padding + (dates[index].time - dates.first().time) * (chartWidth - padding * 2) / (dates.last().time - dates.first().time),
                    y = chartHeight - padding - ((chartData.value - minValue) / (maxValue - minValue) * (chartHeight - padding * 2))
                )
            }
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { point ->
                lineTo(point.x, point.y)
            }
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw points on the line graph
        data.forEachIndexed { index, chartData ->
            val x = padding + (dates[index].time - dates.first().time) * (chartWidth - padding * 2) / (dates.last().time - dates.first().time)
            val y = chartHeight - padding - ((chartData.value - minValue) / (maxValue - minValue) * (chartHeight - padding * 2))
            drawCircle(
                color = primaryColor,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
            )
        }

//        // Draw X and Y axis labels
//        drawContext.canvas.nativeCanvas.drawText(
//            xLabel,
//            chartWidth / 2,
//            chartHeight + 40.dp.toPx(),
//            android.graphics.Paint().apply {
//                color = textColor.toArgb()
//                textSize = 16.sp.toPx()
//                textAlign = android.graphics.Paint.Align.CENTER
//            }
//        )
//
//        drawContext.canvas.nativeCanvas.drawText(
//            yLabel,
//            padding - 40.dp.toPx(),
//            chartHeight / 2,
//            android.graphics.Paint().apply {
//                color = textColor.toArgb()
//                textSize = 16.sp.toPx()
//                textAlign = android.graphics.Paint.Align.CENTER
//                textAlign = android.graphics.Paint.Align.LEFT
//            }
//        )
    }
}
