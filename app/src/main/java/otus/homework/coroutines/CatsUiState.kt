package otus.homework.coroutines

sealed class CatsUiState {
    object Loading: CatsUiState()

    data class Success(val content: CatsContent): CatsUiState()

    data class Error(val errorMessage: String): CatsUiState()
}