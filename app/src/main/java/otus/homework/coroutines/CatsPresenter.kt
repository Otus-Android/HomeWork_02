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

interface ICatsPresenter {
    fun onInitComplete()
    fun attachView(catsView: ICatsView)
    fun detachView()
}

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService,
): ICatsPresenter {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope(CoroutineName("CatsCoroutine"))
    private var workJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onInitComplete() {
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
                    withContext(Dispatchers.Main) {
                        _catsView?.populate(
                            factDeferred.await(),
                            imageDeferred.await()
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

    override fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun detachView() {
        _catsView = null
        CrashMonitor.trackWarning("Stop detachView")
        presenterScope.cancel()
    }
}