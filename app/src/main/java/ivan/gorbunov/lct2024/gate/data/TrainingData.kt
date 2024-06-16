package ivan.gorbunov.lct2024.gate.data

import ivan.gorbunov.lct2024.ui.screens.client.training.exercise_execution.WorkoutState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Exercise(
    val id: Int,
    val name: String,
    val muscle: String,
    @SerialName("additional_muscle")
    val additionalMuscle: String,
    val exercise_type: String = "",
    val type: WorkoutState? = null,
    val equipment: String,
    val difficulty: String,
    val photos: List<PhotoExerciseData>,
    val roundCount: Int? = 3,
    val measurements: List<String>? = emptyList(),
    val plannedRepetitions: Int? = null,
    val plannedWeight: Int? = null,
    val plannedDistance: Int? = null,
    val plannedDuration: Int? = null,
    var actualRepetitions: Int? = null,
    var actualWeight: Float? = null,
    var actualDistance: Float? = null,
    var actualDuration: Int? = null,
    val order: Int? = null
)

@Serializable
data class PhotoExerciseData(
    val url: String = ""
)

@Serializable
data class ExerciseListResponse(
    val exercises: List<Exercise>,
)

@Serializable
data class Training(
    val id: String = "",
    val name: String,
    val roundCount: Int = 3,
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
            id = 1,
            name = "Jumping Jacks",
            muscle = "Full Body",
            additionalMuscle = "Cardio",
            type = WorkoutState.Duration,
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedRepetitions = 30,
            order = 1
        ),
        Exercise(
            id = 2,
            name = "Arm Circles",
            muscle = "Shoulders",
            additionalMuscle = "Arms",
            type =  WorkoutState.Duration,
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedRepetitions = 20,
            order = 2
        )
    ),
    exercises = listOf(
        Exercise(
            id = 3,
            name = "Squats",
            muscle = "Legs",
            additionalMuscle = "Glutes",
            type =  WorkoutState.Self,
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedRepetitions = 15,
            plannedWeight = 20,
            order = 1
        ),
        Exercise(
            id = 4,
            name = "Push-ups",
            muscle = "Chest",
            additionalMuscle = "Triceps",
            type =  WorkoutState.Self,
            equipment = "None",
            difficulty = "Intermediate",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedRepetitions = 20,
            order = 2
        ),
        Exercise(
            id = 5,
            name = "Plank",
            muscle = "Core",
            additionalMuscle = "Back",
            type =  WorkoutState.Duration,
            equipment = "None",
            difficulty = "Advanced",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedDuration = 60,
            order = 3
        )
    ),
    warmDown = listOf(
        Exercise(
            id = 6,
            name = "Child's Pose",
            muscle = "Back",
            additionalMuscle = "Shoulders",
            type =  WorkoutState.Duration,
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedDuration = 60,
            order = 1
        ),
        Exercise(
            id = 7,
            name = "Hamstring Stretch",
            muscle = "Legs",
            additionalMuscle = "Glutes",
            type =  WorkoutState.Duration,
            equipment = "None",
            difficulty = "Beginner",
            photos = listOf(
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
                PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
            ),
            plannedDuration = 60,
            order = 2
        )
    )
)

val mockExercises = listOf<Exercise>(
    Exercise(
        id = 8,
        name = "Приседания",
        muscle = "Ноги",
        additionalMuscle = "Ягодицы",
        type =  WorkoutState.Weight,
        equipment = "Гантели",
        difficulty = "Средний",
        photos = listOf(
            PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
            PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
        ),
        plannedRepetitions = 10,
        plannedWeight = 20,
        order = 1
    ),
    Exercise(
        id = 9,
        name = "Жим лёжа",
        muscle = "Грудь",
        additionalMuscle = "Трицепс",
        type =  WorkoutState.Weight,
        equipment = "Штанга",
        difficulty = "Средний",
        photos = listOf(
            PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9ae2a9e303622813140b47b53f6f2f6c.jpeg"),
            PhotoExerciseData("http://176.123.166.61:5000/media/exercise/image/9287449b4d791d6e30459979206bde33.jpeg")
        ),
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
        warmUp = listOf(),
        exercises = mockExercises,
        warmDown = listOf()
    ),
    Training(
        id = "2",
        name = "Тренировка для продвинутых",
        date = "2024-06-02", // Пример даты
        description = "Тренировка для продвинутых пользователей, включает сложные упражнения для максимальной нагрузки.",
        warmUp = listOf(),
        exercises = mockExercises.reversed(),
        warmDown = listOf()
    )
)

@Serializable
data class CreateTrainingData(
    val name: String,
    val description: String,
    val exercises: List<CreateTrainingExercise>
)

@Serializable
data class CreateTrainingExercise(
    val id: Int,
    val order: Int,
    val exerciseType: String
)
