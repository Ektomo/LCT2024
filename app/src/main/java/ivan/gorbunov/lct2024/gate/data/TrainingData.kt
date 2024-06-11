package ivan.gorbunov.lct2024.gate.data

data class Exercise(
    val id: String,
    val name: String,
    val muscle: String,
    val additionalMuscle: String,
    val type: String,
    val equipment: String,
    val difficulty: String,
    val photos: List<String>,
    val plannedRepetitions: Int? = null,
    val plannedWeight: Int? = null,
    val plannedDistance: Int? = null,
    val plannedDuration: Int? = null,
    var actualRepetitions: Int? = null,
    var actualWeight: Float? = null,
    var actualDistance: Float? = null,
    var actualDuration: Int? = null,
    val order: Int
)

data class Training(
    val id: String = "",
    val name: String,
    val date: String,
    val description: String,
    val warmUp: List<Exercise>,
    val exercises: List<Exercise>,
    val warmDown: List<Exercise>
)

val trainingMock = Training(
    id = "1",
    name = "Full Body Workout",
    date = "2024-06-01", // Пример даты
    description = "Эта тренировка предназначена для новичков, включает базовые упражнения для всего тела.",
    warmUp = listOf(
        Exercise(
            id = "wu1",
            name = "Jumping Jacks",
            muscle = "Full Body",
            additionalMuscle = "Cardio",
            type = "Warm-up",
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedRepetitions = 30,
            order = 1
        ),
        Exercise(
            id = "wu2",
            name = "Arm Circles",
            muscle = "Shoulders",
            additionalMuscle = "Arms",
            type = "Warm-up",
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedRepetitions = 20,
            order = 2
        )
    ),
    exercises = listOf(
        Exercise(
            id = "ex1",
            name = "Squats",
            muscle = "Legs",
            additionalMuscle = "Glutes",
            type = "Strength",
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedRepetitions = 15,
            plannedWeight = 20,
            order = 1
        ),
        Exercise(
            id = "ex2",
            name = "Push-ups",
            muscle = "Chest",
            additionalMuscle = "Triceps",
            type = "Strength",
            equipment = "None",
            difficulty = "Intermediate",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedRepetitions = 20,
            order = 2
        ),
        Exercise(
            id = "ex3",
            name = "Plank",
            muscle = "Core",
            additionalMuscle = "Back",
            type = "Isometric",
            equipment = "None",
            difficulty = "Advanced",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedDuration = 60,
            order = 3
        )
    ),
    warmDown = listOf(
        Exercise(
            id = "wd1",
            name = "Child's Pose",
            muscle = "Back",
            additionalMuscle = "Shoulders",
            type = "Stretch",
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedDuration = 60,
            order = 1
        ),
        Exercise(
            id = "wd2",
            name = "Hamstring Stretch",
            muscle = "Legs",
            additionalMuscle = "Glutes",
            type = "Stretch",
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
            ),
            plannedDuration = 60,
            order = 2
        )
    )
)

val mockExercises = listOf(
    Exercise(
        id = "1",
        name = "Приседания",
        muscle = "Ноги",
        additionalMuscle = "Ягодицы",
        type = "Силовое",
        equipment = "Гантели",
        difficulty = "Средний",
        photos = listOf("https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"),
        plannedRepetitions = 10,
        plannedWeight = 20,
        order = 1
    ),
    Exercise(
        id = "2",
        name = "Жим лёжа",
        muscle = "Грудь",
        additionalMuscle = "Трицепс",
        type = "Силовое",
        equipment = "Штанга",
        difficulty = "Средний",
        photos = listOf("https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"),
        plannedRepetitions = 8,
        plannedWeight = 40,
        order = 2
    )
)

val mockTrainings = listOf(
    Training(
        id = "1",
        name = "Тренировка для новичков",
        date = "2024-06-01", // Пример даты
        description = "Эта тренировка предназначена для новичков, включает базовые упражнения для всего тела.",
        warmUp = listOf(mockExercises[0]),
        exercises = mockExercises,
        warmDown = listOf(mockExercises[1])
    ),
    Training(
        id = "2",
        name = "Тренировка для продвинутых",
        date = "2024-06-02", // Пример даты
        description = "Тренировка для продвинутых пользователей, включает сложные упражнения для максимальной нагрузки.",
        warmUp = listOf(mockExercises[1]),
        exercises = mockExercises.reversed(),
        warmDown = listOf(mockExercises[0])
    )
)