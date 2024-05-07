package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class CatsViewModel(
    private val catFactService: CatFactService,
    private val serviceImage: CatImageService,
) : ViewModel() {

    private val liveData = MutableLiveData<Result>()

    fun getData(): LiveData<Result> {
        return liveData
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, error -> errorParser(error) }

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        viewModelScope.launch((exceptionHandler)) {
            runCatching {
                val fact = async { catFactService.getCatFact() }
                val image = async { serviceImage.getCatImage() }
                CatModel(fact.await(), image.await())
            }
                .onSuccess { liveData.value = Success(it) }
                .onFailure { error -> errorParser(error) }
        }
    }

    private fun errorParser(error: Throwable) {
        when (error) {
            java.net.SocketTimeoutException() -> liveData.value = TimeoutException
            else -> {
                liveData.value = Error(error.message)
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }

    class CatsViewModelFactory(
        private val catFactService: CatFactService,
        private val serviceImage: CatImageService,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CatsViewModel(catFactService, serviceImage) as T
    }
}