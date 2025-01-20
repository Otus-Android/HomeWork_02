package otus.homework.coroutines

sealed class Result {
    object Initialization : Result()
    data class Success(val dataModel: DataModel) : Result()
    data class Error(val text: String) : Result()
}