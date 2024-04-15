package otus.homework.coroutines

sealed class Result {
    class Success<T>(val value: T) : Result()
    object Error : Result()
}