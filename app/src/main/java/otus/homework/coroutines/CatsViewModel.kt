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

    private val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is SocketTimeoutException) _catsModel.value = Error
        else CrashMonitor.trackWarning()
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
       viewModelScope.launch(context = handler + CoroutineName(COROUTINE_NAME)) {
            val fact = async { catsService.getCatFact() }.await()
            val image = async { imageCatService.getCatImage() }.await()
            if (fact.isSuccessful && image.isSuccessful) {
                _catsModel.value = Success(
                    ModelFact(
                        fact = fact.body(),
                        imageCat = image.body()?.first()
                    )
                )
            } else {
                _catsModel.value = Error
            }
        }
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}

class CatsViewModelFactory( private val catsService: CatsService,
                            private val imageCatService: ImageCatService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imageCatService) as T
}