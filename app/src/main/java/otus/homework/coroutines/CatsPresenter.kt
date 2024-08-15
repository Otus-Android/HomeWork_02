package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val catFact = async { catsService.getCatFact() }
                _catsView?.populate(catFact.await())
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> return@launch
                    is SocketTimeoutException -> _catsView?.showTimeoutError()
                    else -> _catsView?.showError(e.message)
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
        job = null
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

}