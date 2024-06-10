package otus.homework.coroutines

sealed class ApiResult<out T: Any> {
    data class Success<out T: Any>(val data: T) : ApiResult<T>()
    data class Error(val error: Throwable) : ApiResult<Nothing>()
}