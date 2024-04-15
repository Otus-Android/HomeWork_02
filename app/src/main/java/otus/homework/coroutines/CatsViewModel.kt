package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService : CatsService,
    private val pictureService : PicturesService,
    private val catsUiStateMapper : CatsUiStateMapper,
) : ViewModel() {

    private var _catsView : ICatsView? = null
    private val exceptionHandler by lazy {
        CoroutineExceptionHandler {_, e ->
            CrashMonitor.trackWarning()
            _catsView?.populateFromVM(Error(e.message ?: "Uknown exception"))
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val catsUiState = getCatsUiState()
                _catsView?.populateFromVM(Success(catsUiState))
            } catch (e : SocketTimeoutException) {
                _catsView?.populateFromVM(Error("Не удалось получить ответ от сервером"))
                throw CancellationException()
            }
        }
    }

    fun attachView(catsView : ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }

    private suspend fun getCatsUiState() : CatsUiState = coroutineScope {
        val fact = async { catsService.getCatFact() }
        val pictures = async { pictureService.getPictures() }

        catsUiStateMapper.map(fact.await(), pictures.await())
    }
}

class CatsViewModelFactory(
    private val catsService : CatsService,
    private val pictureService : PicturesService,
    private val catsUiStateMapper : CatsUiStateMapper,
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass : Class<T>) : T =
        CatsViewModel(
            catsService,
            pictureService,
            catsUiStateMapper
        ) as T
}

sealed class Result
data class Success<T>(val data : T) : Result()
data class Error(val message: String) : Result()
