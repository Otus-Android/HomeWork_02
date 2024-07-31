package otus.homework.coroutines

sealed class Result
data class Success(val modelFact: ModelFact) : Result()
object Error : Result()