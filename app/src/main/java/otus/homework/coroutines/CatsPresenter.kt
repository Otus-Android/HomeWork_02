package otus.homework.coroutines

import android.content.res.Resources
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: CatImageService,
    private val resManager: Resources
) {

    private var _catsView: ICatsView? = null

    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    private var requestCount = 0

    fun onInitComplete() {
        job?.cancel()
        job = scope.launch {
            requestCount++
            try {
                val result = coroutineScope {
                    val fact = async {
                        if (requestCount % 2 == 0) {
                            throw SocketTimeoutException()
                        }
                        catsService.getCatFact()
                    }
                    val image = async {
                        imageService.getCatImages().firstOrNull()
                    }
                    FactState(
                        fact = fact.await(),
                        image = image.await()
                    )
                }
                _catsView?.populate(result)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> return@launch
                    is SocketTimeoutException -> {
                        _catsView?.showError(resManager.getString(R.string.socket_timeout_error))
                    }

                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showError(
                            e.message ?: resManager.getString(R.string.unknown_error)
                        )
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null

        job?.cancel()
        job = null
    }
}