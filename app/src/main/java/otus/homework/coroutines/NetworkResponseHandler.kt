package otus.homework.coroutines

import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> Response<T>.handleApi(): NetworkResult<T> {
    return try {
        if (isSuccessful && body() != null) {
            NetworkResult.Success(data = body() as T)
        } else {
            NetworkResult.Error(error = errorBody())
        }
    } catch (e: HttpException) {
        NetworkResult.Error(error = errorBody())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}