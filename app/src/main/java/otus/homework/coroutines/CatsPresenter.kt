package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsImageService
import otus.homework.coroutines.domain.Cat
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) : CoroutineScope {

    private var job: Job? = null

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        job = launch {
            try {
                val cat = Cat(
                    fact = catsFactService.getCatFact().fact,
                    imageUrl = catsImageService.getImage().first().url
                )
                _catsView?.populate(cat)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToastFailedToResponse()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToastDefaultFailed(e)
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