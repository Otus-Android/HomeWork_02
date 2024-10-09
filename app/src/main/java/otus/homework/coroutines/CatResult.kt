package otus.homework.coroutines

sealed class Result
data class Success(val catData: CatData) : Result()
data class Error(val message: String) : Result()