package otus.homework.coroutines

sealed class CatResult {
    class Success(val cat: Cat) : CatResult()
    class Error(val message: String?) : CatResult()
    object TimeoutError : CatResult()
    object Init : CatResult()
    object Loading : CatResult()
}