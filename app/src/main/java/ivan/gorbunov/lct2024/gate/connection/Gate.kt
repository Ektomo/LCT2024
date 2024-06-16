package ivan.gorbunov.lct2024.gate.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ivan.gorbunov.lct2024.gate.data.User
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

enum class HttpMethod {
    GET, POST, PUT, DELETE
}

@Singleton
class Gate @Inject constructor(
    private val client: OkHttpClient,
    private val baseUrl: String,
    private val context: Context
) {

    private var token: String? = null
    var format = JsonSerializer.format
    var user: User? = null

    fun setToken(token: String) {
        this.token = token
    }

//    fun setUser(user: User){
//        this.user = user
//    }
//
//    fun getUser() = user

    inline fun <reified T> makeSyncRequest(
        url: String,
        body: T?,
        type: HttpMethod = HttpMethod.POST
    ): String {
        if (!isNetworkAvailable()) {
            throw AppNetworkException()
        }
        val json = format.encodeToString(value = body)
        return request(url, type, json)
    }


    fun request(
        path: String,
        method: HttpMethod,
        json: String? = null,
//        body: RequestBody? = null
    ): String {
        val url = "$baseUrl$path"
        val requestBuilder = Request.Builder()
            .url(url)

        val contentType = "application/json".toMediaType()
        val body = json?.toRequestBody(contentType)

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        when (method) {
            HttpMethod.GET -> requestBuilder.get()
            HttpMethod.POST -> requestBuilder.post(body!!)
            HttpMethod.PUT -> requestBuilder.put(body!!)
            HttpMethod.DELETE -> requestBuilder.delete(body)
        }

        val request = requestBuilder.build()
        return processResponse(client.newCall(request).execute())
    }

    private fun processResponse(r: Response?, error: Throwable? = null): String {
        return if (r != null) {
            when (r.code) {
                401 -> throw AppLoginException()
                404 -> throw AppResponseException("Сервер не доступен или не существует по заданному адресу")
            }
            val response = r.body!!.string()
            response
        } else {
            throw AppException(
                "Ошибка запроса, ${error?.localizedMessage ?: ""}"
            )
        }
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}

class AppNetworkException(override var msg: String = "Нет соединения") : AppException(msg)

class AppResponseException(override var msg: String, override val stackTrace: String = "") : AppException(msg, stackTrace)

class AppLoginException(override var msg: String = "Неверный логин или пароль", override val stackTrace: String = "") : AppException(
    msg,
    stackTrace
)

open class AppException(open var msg: String, open val stackTrace: String = "") : java.lang.Exception(msg) {

    init {
        println()
    }

    fun addMessage(value: String) {
        msg += value
    }
}