package otus.homework.coroutines

import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val connectionErrorHandler: ConnectionErrorHandler
) {

    private var _catsView: ICatsView? = null
    private var presenterScope: PresenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                fetchCatFact()
            } catch (e: SocketTimeoutException) {
                connectionErrorHandler.onError()
            } catch(e: Exception) {
                CrashMonitor.trackWarning(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    private suspend fun fetchCatFact() {
        val fact = catsService.getCatFact()
        _catsView?.populate(fact)
    }

    fun cancelFetch() = presenterScope.cancel()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    companion object {
        private const val UNKNOWN_ERROR = "Unknown error"
    }
}