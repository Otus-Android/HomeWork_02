package otus.homework.coroutines


sealed class Result {

    data class Success(val catModel: CatModel) : Result()
    data class Error(val message: String) : Result()
}
