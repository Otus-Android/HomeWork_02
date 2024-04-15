package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val serviceCatImage: CatsService
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
            val responseCatFact = catsService.getCatFact()
            val responseCatImage = serviceCatImage.getCatImage()

            if (
                responseCatFact.isSuccessful && responseCatFact.body() != null &&
                responseCatImage.isSuccessful && responseCatImage.body() != null
            ) {
                _catLiveData.value = Success(responseCatFact.body()!!, responseCatImage.body()!!)
            } else {
                _catLiveData.value = Error("Не удалось загрузить данные")
            }
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val serviceCatImage: CatsService
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, serviceCatImage) as T
}

sealed class Result
data class Success(val fact: Fact, val catImage: List<CatImage>) : Result()
data class Error(val message: String) : Result()