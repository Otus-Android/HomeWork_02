package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope(CoroutineName("CatsCoroutine"))
    private var workJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onInitComplete() {
        if (workJob?.isActive == true) {
            _catsView?.handle(R.string.cats_wait_next_fact)
            return
        }
        workJob = presenterScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val factDeferred = async {
                        catsService.getCatFact()
                    }
                    val imageDeferred = async {
                        catImageService.getRandomImages()
                    }
                    factDeferred.await()
                    imageDeferred.await()
                    withContext(Dispatchers.Main) {
                        _catsView?.populate(
                            factDeferred.getCompleted(),
                            imageDeferred.getCompleted()
                        )
                    }
                }
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