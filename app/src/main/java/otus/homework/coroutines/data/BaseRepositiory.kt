package otus.homework.coroutines.data

import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.Utils
import retrofit2.Response

abstract class BaseRepository {

    protected val network = NetworkService().createRetrofit

    sealed class Result<out T : Any?> {
        data class Success<out T : Any?>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }

    protected suspend fun <T : Any> handleApi(
        block: suspend () -> Response<T>,
    ): Result<T> {
        try {
            val response = block.invoke()
            return when {
                response.isSuccessful -> handleSuccess(response)
                else -> handleError(response)
            }
        } catch (e: Exception) {
            Utils.log { "[Repository]: handled error - ${e.message}" }
            return Result.Error(e)
        }
    }

    private fun <T : Any> handleSuccess(
        response: Response<T>
    ): Result<T> {
        response.body()?.let {
            Utils.log { "[Repository]: request is successful" }
            return Result.Success(it)
        }

        CrashMonitor.trackWarning("[Repository]: request success - empty body")

        return Result.Error(RuntimeException("Success with empty body"))
    }

    private fun <T : Any> handleError(
        response: Response<T>
    ): Result<T> {
        response.errorBody()?.let { body ->
            try {
                val json = body.string()
                val error = JSONObject(json)
                val message =
                    if (error.has("message"))
                        error.getString("message")
                    else
                        "Error empty body"

                CrashMonitor.trackWarning("[Repository]: request error - $message")

                return Result.Error(
                    RuntimeException(message)
                )
            } catch (ignored: JsonSyntaxException) {
                Utils.log { "[Repository]: JsonSyntaxException" }
                return Result.Error(RuntimeException("Internal json error"))
            }
        }

        CrashMonitor.trackWarning("[Repository]: request failed - Unknown Error")

        return Result.Error(RuntimeException("Unknown Error"))
    }
}