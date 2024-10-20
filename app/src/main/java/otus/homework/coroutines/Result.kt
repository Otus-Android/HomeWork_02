package otus.homework.coroutines

sealed interface Result<T> {
    data class Success<T>(val mainUiModel: T) : Result<T>
    data class Error<T>(val error: Throwable) : Result<T>
    class Noting<T> : Result<T>
}