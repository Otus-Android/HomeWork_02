package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.CoroutineExceptionHandler

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageLinkService: CatsImageLinkService
) : ViewModel() {
    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData
    private val crashMonitor = CrashMonitor
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        crashMonitor.trackWarning()
        _catsLiveData.value = when(throwable) {
            is java.net.SocketTimeoutException -> Result.Error("Не удалось получить ответ от сервера")
            else -> Result.Error(throwable.message?:"Ошибка получения данных")
        }
    }

    init {
        getCatsViewData()
    }

    fun getCatsViewData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val fact: Deferred<String> = async { catsService.getCatFact().fact }
            val imgUrl: Deferred<String> = async { catsImageLinkService.getCatImageLink().first().url }
            _catsLiveData.value = Result.Success( CatsViewData(fact.await(), imgUrl.await()) )
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val catsImageLinkService: CatsImageLinkService
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, catsImageLinkService) as T
}

sealed class Result {
    data class Success(val fact: CatsViewData) : Result()
    data class Error(val errorMessage: String) : Result()
}