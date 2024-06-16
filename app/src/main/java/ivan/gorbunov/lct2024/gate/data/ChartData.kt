package ivan.gorbunov.lct2024.gate.data

import ivan.gorbunov.lct2024.ui.screens.client.progress.ChipParameter

data class ChartData(val date: String, val value: Float)
data class Parameter(override val name: String): ChipParameter
data class Quote(val text: String)
data class Comment(val text: String, val author: String)

val parameters = listOf(
    Parameter("Вес"),
    Parameter("Силовые показатели"),
    Parameter("Беговые показатели"),
    Parameter("Процент жира"),
    Parameter("Мышечная масса")
)

val weightData = listOf(
    ChartData("01.01", 70f),
    ChartData("02.01", 71f),
    ChartData("03.01", 69f),
    ChartData("04.01", 68f),
    ChartData("05.01", 67f),
    ChartData("08.01", 64f),
    ChartData("10.01", 61f),
    ChartData("12.01", 63f)
)

val strengthData = listOf(
    ChartData("01.01", 100f),
    ChartData("02.01", 105f),
    ChartData("03.01", 102f),
    ChartData("04.01", 107f),
    ChartData("05.01", 110f)
)

val runData = listOf(
    ChartData("01.01", 5f),
    ChartData("02.01", 5.2f),
    ChartData("03.01", 5.1f),
    ChartData("04.01", 5.3f),
    ChartData("05.01", 5.5f)
)
