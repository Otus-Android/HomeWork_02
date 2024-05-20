package otus.homework.coroutines.model


sealed class Result {
    class Success<T>(data: T): Result()

    class Error(message: String): Result()
}