package otus.homework.coroutines.domain

sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val error: String) : Result()
}