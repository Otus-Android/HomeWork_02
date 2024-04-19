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
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) : ViewModel() {

    private val _liveData = MutableLiveData<Result>()
    val liveData: LiveData<Result> = _liveData

    fun onInitComplete() = loadData()

    fun onButtonClick() = loadData()

    private fun loadData() {
        val handler = getCoroutineHandler()
        viewModelScope.launch(handler) {
            val factDeferred = async { getFact() }
            val imageDeferred = async { getFirstImage() }

            val cat = getPresentationCat(factDeferred.await(), imageDeferred.await())
            _liveData.value = Result.Success(cat)
        }
    }

    private fun getCoroutineHandler(): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable is SocketTimeoutException) {
                _liveData.value = Result.Error.SocketTimeout
            } else if (throwable is Exception && throwable.message != null) {
                _liveData.value = Result.Error.Other(throwable.message!!)
            }
            CrashMonitor.trackWarning(throwable)
        }

    private suspend fun getFact(): Fact {
        return catsService.getCatFact()
    }

    private suspend fun getFirstImage(): Image {
        return imagesService.getImages().first()
    }

    private fun getPresentationCat(fact: Fact, image: Image) =
        CatPresentation(
            fact = fact.fact,
            imageUrl = image.url
        )
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imagesService) as T
}

sealed class Result {
    data class Success(val cat: CatPresentation) : Result()
    sealed class Error : Result() {
        object SocketTimeout : Error()
        data class Other(val message: String) : Error()
    }
}
