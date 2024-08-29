package otus.homework.coroutines

sealed class Result
data class Success(val cat: CatData): Result()
data class Error(val error:String?): Result()