package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
): CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext = CoroutineName("CatsCoroutine") + job
    private var _catsView: ICatsView? = null


    fun onInitComplete() {
        launch {
            try {
                val fact = catsService.getCatFact()
                withContext(Dispatchers.Main) {
                    _catsView?.populate(fact)
                }
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.onError(R.string.connection_error_message)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                    }
                }
            }

        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancel()
        _catsView = null
    }
}