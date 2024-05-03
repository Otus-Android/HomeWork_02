package otus.homework.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: PresenterScope,
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            runCatching { catsService.getCatFact() }
                .onSuccess { _catsView?.populate(it) }
                .onFailure { errorParser(it) }
        }
    }

    private fun errorParser(error: Throwable) {
        when (error) {
            java.net.SocketTimeoutException() -> _catsView?.showToast()
            else -> {
                _catsView?.showToast(error.message.orEmpty())
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}