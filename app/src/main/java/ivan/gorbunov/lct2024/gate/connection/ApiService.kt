package ivan.gorbunov.lct2024.gate.connection

import ivan.gorbunov.lct2024.gate.data.ChatHistory
import ivan.gorbunov.lct2024.gate.data.Exercise
import ivan.gorbunov.lct2024.gate.data.ExerciseListResponse
import ivan.gorbunov.lct2024.gate.data.LoginRequest
import ivan.gorbunov.lct2024.gate.data.TokenResponse
import ivan.gorbunov.lct2024.gate.data.TrainersResponse
import ivan.gorbunov.lct2024.gate.data.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(private val gate: Gate) {

    private val format = JsonSerializer.format

    fun login(login: String, password: String) {
        val body = LoginRequest(login, password)
        val response = gate.makeSyncRequest("users/auth", body, HttpMethod.POST)
        val tokenResponse = format.decodeFromString<TokenResponse>(response)
        gate.setToken(tokenResponse.accessToken)
    }

    fun register(login: String, password: String) {
        val body = LoginRequest(login, password)
        val response = gate.makeSyncRequest("users/registration", body, HttpMethod.POST)
        val user = format.decodeFromString<User>(response)
        login(login, password)
    }

    fun getAboutMe(): User {
        val response = gate.makeSyncRequest<Unit>("users/me", null, HttpMethod.GET)
        val user = format.decodeFromString<User>(response)
        gate.user = user
        return user
    }

    fun getTrainers(): List<User> {
        val response = gate.makeSyncRequest<Unit>("users/trainers", null, HttpMethod.GET)
        return format.decodeFromString<TrainersResponse>(response).result
    }

    fun getClients(): List<User> {
        val response = gate.makeSyncRequest<Unit>("users/clients", null, HttpMethod.GET)
        return format.decodeFromString<TrainersResponse>(response).result
    }

    fun getChatHistory(receiverId: Int, page: Int, size: Int): ChatHistory {
        val response = gate.makeSyncRequest<Unit>(
            "chats/history?receiver_id=$receiverId&page=$page&size=$size",
            null,
            HttpMethod.GET
        )
        return format.decodeFromString(response)
    }

    fun getUser() = gate.user!!

    fun getExercises(
        page: Int,
        size: Int,
        nameContains: String? = null,
        muscleContains: String? = null,
        additionalMuscleContains: String? = null,
        exerciseTypeContains: String? = null,
        equipmentContains: String? = null,
        difficultyContains: String? = null
    ): List<Exercise> {

        val urlBuilder = StringBuilder("exercises/types?page=$page&size=$size")

        nameContains?.let { urlBuilder.append("&name__icontains=$it") }
        muscleContains?.let { urlBuilder.append("&muscle__icontains=$it") }
        additionalMuscleContains?.let { urlBuilder.append("&additional_muscle__icontains=$it") }
        exerciseTypeContains?.let { urlBuilder.append("&exercise_type__icontains=$it") }
        equipmentContains?.let { urlBuilder.append("&equipment__icontains=$it") }
        difficultyContains?.let { urlBuilder.append("&difficulty__icontains=$it") }

        val response = gate.makeSyncRequest<Unit>(urlBuilder.toString(), null, HttpMethod.GET)
        return format.decodeFromString<ExerciseListResponse>(response).exercises
    }

//    fun getClientTrainings()

}

object JsonSerializer {

    @OptIn(ExperimentalSerializationApi::class)
    val format = Json {
        prettyPrint = true // Удобно печатает в несколько строчек
        ignoreUnknownKeys = true // Неизвестные значение
        coerceInputValues = true // Позволяет кодировать в параметрах null
        explicitNulls = true // Позволяет декодировать в параметрах null
    }
}