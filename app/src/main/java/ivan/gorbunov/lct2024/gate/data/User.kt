package ivan.gorbunov.lct2024.gate.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

//data class User(
//    val id: String,
//    val name: String,
//    val surname: String,
//    val url: String = "https://i.imgur.com/CzXTtJV.jpg",
//    val email: String = ""
//)
@Serializable
data class User(
    val id: Int,
    val email: String? = "",
    val phone: String? = "",
    val name: String? = "",
    val surname: String? = "",
    val patronymic: String? = "",
    val gender: String? = "",
    val role: String? = "",
    val age: Int? = 0,
    val weight: Float? = 0f,
    val height: Float? = 0f,
    val rate: Float? = 0f,
    val description: String? = "",
    val url: String? = "https://i.imgur.com/CzXTtJV.jpg",
    val avatar_path: String? = ""
)

@Serializable
data class TrainersResponse(
    val result: List<User>
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String
)



val mockUsers = listOf(
    User(
        id = 1,
        name = "Иван",
        surname = "Иванов",
        email = "ivan@example.com"
    ),
    User(
        id = 2,
        name = "Петр",
        surname = "Петров",
        email = "petr@example.com"
    ),
    User(
        id = 3,
        name = "Сидор",
        surname = "Сидоров",
        email = "sidor@example.com"
    ),
    User(
        id = 4,
        name = "Алексей",
        surname = "Алексеев",
        email = "alexey@example.com"
    )
)
