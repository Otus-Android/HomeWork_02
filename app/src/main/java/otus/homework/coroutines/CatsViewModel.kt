package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) : ViewModel() {
    private val _catLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catLiveData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is SocketTimeoutException) {
            _catLiveData.value = Error("Не удалось получить ответ от сервера")
        } else {
            CrashMonitor.trackWarning()
            _catLiveData.value = Error("${throwable.message}")
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val deferredCatFact = async { catsFactService.getCatFact() }
            val deferredCatImages = async { catsImageService.getCatImage() }
            val сatFact = deferredCatFact.await()
            val сatImages = deferredCatImages.await()

            _catLiveData.value = Success(сatFact, сatImages)
        }
    }
}

class CatsViewModelFactory(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsFactService, catsImageService) as T
}

sealed class Result
data class Success(val fact: Fact, val catImage: List<CatImage>) : Result()
data class Error(val message: String) : Result()