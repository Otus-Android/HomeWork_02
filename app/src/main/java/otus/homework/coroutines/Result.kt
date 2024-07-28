package otus.homework.coroutines

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    sealed class Error(val exception: Throwable) : Result<Nothing>() {
        class ServerError(exception: Throwable) : Error(exception)
        class GeneralError(exception: Throwable) : Error(exception)
    }
}