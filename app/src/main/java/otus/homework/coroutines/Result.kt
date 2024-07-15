package otus.homework.coroutines

sealed class Result {
    data class Success<T>(val body: T) : Result()

    sealed class Error : Result() {
        object SocketError : Error()
        data class OtherError(val e: Throwable) : Error()
    }
}