package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(
    private val catsService: CatsService,
    private val catImageService: CatImageService,
) : ViewModel(), ICatsPresenter {

    private var _catsView: ICatsView? = null
    private var workJob: Job? = null
    private val scopeExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        val error = when (throwable) {
            is SocketTimeoutException -> Result.Error(errorResId = R.string.app_request_timeout)
            is CancellationException -> null
            else -> Result.Error(errorMsg = throwable.message.toString())
        }
        error?.let { e ->
            _catsView?.populate(e)
            CrashMonitor.trackError(
                throwable, "Handled by VMCoroutineExceptionHandler $coroutineContext"
            )
        }
    }

    override fun onInitComplete() {
        if (workJob?.isActive == true) {
            _catsView?.handle(R.string.cats_wait_next_fact)
            return
        }
        workJob = viewModelScope.launch(scopeExceptionHandler) {
            withContext(Dispatchers.IO) {
                val factDeferred = async {
                    catsService.getCatFact()
                }
                val imageDeferred = async {
                    catImageService.getRandomImages()
                }
                withContext(Dispatchers.Main) {
                    _catsView?.populate(
                        Result.Success(
                            CatData(
                                factDeferred.await(),
                                imageDeferred.await()
                            )
                        )
                    )
                }
            }
        }
    }

    override fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }
}