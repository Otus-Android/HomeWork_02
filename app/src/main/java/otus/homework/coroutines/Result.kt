package otus.homework.coroutines

sealed class Result
data class Success(val catsModel: CatsModel) : Result()
data class Error(val message: String) : Result()