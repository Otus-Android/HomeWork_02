package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) : ViewModel() {

    private val _liveData by lazy { MutableLiveData<Result>() }
    val liveData: LiveData<Result> = _liveData

    fun onInitComplete() = loadData()

    fun onButtonClick() = loadData()

    private fun loadData() {
        val handler = getCoroutineHandler()
        viewModelScope.launch(handler) {
            val factDeferred = async { getFact() }
            val imageDeferred = async { getFirstImage() }

            val cat = getPresentationCat(factDeferred.await(), imageDeferred.await())
            _liveData.postValue(Result.Success(cat))
        }
    }

    private fun getCoroutineHandler(): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable is SocketTimeoutException) {
                _liveData.postValue(Result.Error.SocketTimeout)
            } else if (throwable is Exception && throwable.message != null) {
                _liveData.postValue(Result.Error.Other(throwable.message!!))
            }
            CrashMonitor.trackWarning(throwable)
        }

    private suspend fun getFact(): Fact {
        var fact: Fact
        withContext(Dispatchers.IO) {
            fact = catsService.getCatFact()
        }
        return fact
    }

    private suspend fun getFirstImage(): Image {
        var image: Image
        withContext(Dispatchers.IO) {
            image = imagesService.getImages().first()
        }
        return image
    }

    private fun getPresentationCat(fact: Fact, image: Image) =
        CatPresentation(
            fact = fact.fact,
            imageUrl = image.url
        )
}

sealed class Result {
    data class Success(val cat: CatPresentation) : Result()
    sealed class Error : Result() {
        object SocketTimeout : Error()
        data class Other(val message: String) : Error()
    }
}
