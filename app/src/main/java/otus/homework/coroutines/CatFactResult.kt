package otus.homework.coroutines

sealed class CatFactResult {
    object NoFact: CatFactResult()
    data class Success(val catFact: CatFact): CatFactResult()
    object SocketTimeoutError: CatFactResult()
    data class Error(val error: String?): CatFactResult()
}