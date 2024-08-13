package otus.homework.coroutines

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService): ViewModel() {

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        if (exception is SocketTimeoutException) {
            _catsLiveData.value = Result.ErrorRes(R.string.error_server)
        } else {
            _catsLiveData.value = Result.Error(exception.localizedMessage)
            CrashMonitor.trackWarning(exception)
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async {
                val response = catsService.getCatFact()
                response.body()!!
            }
            val image = async {
                val response = catsService.getCatImages()
                response.body()!!
            }
            val cat = CatModel(fact.await().fact, image.await().randomOrNull()?.url)
            _catsLiveData.value = Result.Success(cat)
        }
    }
}

class CatsViewModelFactory(private val catsRepository: CatsService) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = CatsViewModel(catsRepository) as T
}

sealed class Result{
    data class Success<CatModel>(val catModel: CatModel) : Result()
    data class Error(val message: String?) : Result()
    data class ErrorRes(@StringRes val message: Int) : Result()
}
