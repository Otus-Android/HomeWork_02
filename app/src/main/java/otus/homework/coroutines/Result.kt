package otus.homework.coroutines

sealed class Result() {
    class Success<T> : Result()
    class Error : Result()
}
