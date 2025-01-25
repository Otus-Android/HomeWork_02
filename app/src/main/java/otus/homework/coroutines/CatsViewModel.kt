package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _state = MutableStateFlow<Result>(Result.Initialization)
    val state = _state.asStateFlow()

    private var job: Job? = null

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            if (throwable is SocketTimeoutException) {
                showToast("Не удалось получить ответ от сервером")
            } else {
                showToast(throwable.message ?: "No message")
                CrashMonitor.trackWarning(throwable)
            }
        }
    }

    fun onInitComplete() {
        job?.cancel()
        job = viewModelScope.launch(exceptionHandler) {
            val factDeferred = async { catsService.getCatFact().body()?.fact }
            val imageDeferred = async { imageService.getImage().body()?.getOrNull(0)?.url }

            val fact = factDeferred.await()
            val image = imageDeferred.await()

            if (!fact.isNullOrEmpty() && !image.isNullOrEmpty()) {
                _state.emit(Result.Success(DataModel(fact.toString(), image)))
            }
        }
    }

    private fun showToast(text: String) {
        viewModelScope.launch {
            _state.emit(Result.Error(text))
        }
    }

    class CatsViewModelFactory(
        private val catsService: CatsService,
        private val imageService: ImageService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatsViewModel(catsService, imageService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}