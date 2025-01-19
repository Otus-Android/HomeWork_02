package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope(CoroutineName("CatsCoroutine"))
    private var workJob: Job? = null

    fun onInitComplete() {
        if (workJob?.isActive == true) {
            _catsView?.handle(R.string.cats_wait_next_fact)
            return
        }
        workJob = presenterScope.launch {
            try {
                val fact = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                _catsView?.populate(fact)
            } catch (timeOutException: SocketTimeoutException) {
                _catsView?.handle(R.string.app_request_timeout)
            } catch (_: CancellationException) {
            } catch (t: Throwable) {
                CrashMonitor.trackError(t)
                _catsView?.handle(t.message.toString())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        CrashMonitor.trackWarning("Stop detachView")
        presenterScope.cancel()
    }
}