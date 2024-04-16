package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageCatService: ImageCatService
) : ViewModel() {

    private val _catsModel = MutableLiveData<Result>()
    val catsModel: LiveData<Result> = _catsModel

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        if (throwable is SocketTimeoutException) _catsModel.value = Error
        else CrashMonitor.trackWarning()
    }
    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(handler + CoroutineName("CatsCoroutine")) {
            val fact = async { catsService.getCatFact() }
            val image = async { imageCatService.getCatImage() }

            val responseFact = fact.await()
            val responseImage = image.await()
            if (responseFact.isSuccessful && responseFact.body() != null
                && responseImage.isSuccessful && responseImage.body() != null
            ) {
                _catsModel.value = Success(
                    ModelPresentation(
                        responseFact.body()!!,
                        responseImage.body()!!.first()
                    )
                )
            }
        }
    }

}


class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageCatService: ImageCatService
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imageCatService) as T
}

sealed class Result
data class Success(val data: ModelPresentation) : Result()
object Error : Result()
