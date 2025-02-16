package otus.homework.coroutines

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: CatsImageService,
    private val context: Application
) : ViewModel() {
    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    init {
        getData()
    }

    fun getData() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
            if (throwable is java.net.SocketTimeoutException) _catsLiveData.value =
                Error(context.getString(R.string.socket_timeout_exception))
            else _catsLiveData.value =
                Error(throwable.message ?: context.getString(R.string.unknown_exception))
        }
        viewModelScope.launch(handler) {
            coroutineScope {
                val fact = async { catsService.getCatFact().fact }
                val image = async { imageService.getCatImage().first() }
                _catsLiveData.value = Success(CatsData(fact.await(), image.await()))
            }
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: CatsImageService,
    private val context: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imageService, context) as T
}

sealed class Result
data class Success(val data: CatsData) : Result()
data class Error(val message: String) : Result()