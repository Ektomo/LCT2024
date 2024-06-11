package ivan.gorbunov.lct2024.gate.data

data class WorkoutItem(
    val id: String,
    val day: String,
    val title: String,
    val description: String,
    val imageUrl: String
)

val exampleWorkouts = listOf(
    WorkoutItem(
        id = "1",
        day = "Понедельник",
        title = "Силовая",
        description = "Назначено тренером",
        imageUrl = "https://i.imgur.com/CzXTtJV.jpg"
    ),
    WorkoutItem(
        id = "2",
        day = "Среда",
        title = "Беговая",
        description = "Назначено самостоятельно (согласовано с тренером)",
        imageUrl = "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
    )
)
