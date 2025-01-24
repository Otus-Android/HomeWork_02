package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class Result {
    data class Success(val cat: Cat): Result()
    data class Error(val exception: Throwable): Result()
    object Loading: Result()
}

class CatsViewModel: ViewModel() {
    private var _uiState: MutableStateFlow<Result> = MutableStateFlow(Result.Loading)
    val uiState: StateFlow<Result> = _uiState
    fun getCat() {
        val handler = CoroutineExceptionHandler { _, exception ->
            _uiState.value = Result.Error(exception)
            CrashMonitor.trackWarning()
        }
        viewModelScope.launch(handler) {
            _uiState.value = Result.Loading
            val di = DiContainer()
            val cat = Cat(di.factsService.getCatFact(), di.picsService.getCatPicture()[0])
            _uiState.value = Result.Success(cat)
        }
    }
}
