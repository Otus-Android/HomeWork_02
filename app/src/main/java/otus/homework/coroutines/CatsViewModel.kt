package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
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
        _catsLiveData.postValue( when(throwable) {
            is java.net.SocketTimeoutException -> Result.Error("Не удалось получить ответ от сервера")
            else -> {
                crashMonitor.trackWarning()
                Result.Error(throwable.message?:"Ошибка получения данных")
            }
        })
    }

    init {
        getCatsViewData()
    }

    fun getCatsViewData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val fact: Deferred<String> = async(Dispatchers.IO)
                { catsService.getCatFact().fact }
            val imgUrl: Deferred<String> = async(Dispatchers.IO) {
                catsImageLinkService.getCatImageLink().first().url
            }
            _catsLiveData.postValue( Result.Success( CatsViewData(fact.await(), imgUrl.await()) ) )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
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