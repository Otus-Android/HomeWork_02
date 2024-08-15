package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catFactsService: CatFactsService,
    private val catImageService: CatImageService
) : ViewModel() {
    private val _uiState = MutableStateFlow<CatResult>(CatResult.Init)
    val uiState: StateFlow<CatResult> = _uiState
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        if (exception is SocketTimeoutException)
            _uiState.value = CatResult.TimeoutError
        else {
            CrashMonitor.trackWarning()
            _uiState.value = CatResult.Error(exception.message)
        }
    }

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.value = CatResult.Loading
        viewModelScope.launch(exceptionHandler) {
            val catFact = async { catFactsService.getCatFact() }
            val catImage = async { catImageService.getCatImages().first() }
            _uiState.value = CatResult.Success(Cat(catFact.await(), catImage.await()))
        }

    }

    fun onMoreClicked() {
        loadData()
    }

    class Factory(
        private val catsFactService: CatFactsService,
        private val catsImageService: CatImageService
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsFactService, catsImageService) as T
        }
    }

}