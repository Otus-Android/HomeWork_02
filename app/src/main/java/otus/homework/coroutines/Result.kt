package otus.homework.coroutines

sealed class Result() {
    class Success<T>(val catInfo: CatInfo) : Result()
    class Error(val error: Throwable) : Result()
}
