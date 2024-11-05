package otus.homework.coroutines

sealed class Result(){
    data class Success(val fact: Fact, val imageUrl: String): Result()
    data class Error(val error: String): Result()
}
