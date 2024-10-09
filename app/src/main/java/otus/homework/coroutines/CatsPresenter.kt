package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val onShowErrorMessage: (Throwable) -> Unit
) {
    private var _catsView: ICatsView? = null
    private var jobFact: Job? = null

    fun onInitComplete() {
        jobFact = CoroutineScope(
            Dispatchers.Main + CoroutineName(COROUTINE_NAME)
        ).launch {
            runCatching {
                catsService.getCatFact()
            }
                .onSuccess {  fact ->
                    _catsView?.populate(fact)
                }
                .onFailure { throwable ->
                    onShowErrorMessage(throwable)

                    if (throwable !is SocketTimeoutException) {
                        CrashMonitor.trackWarning(throwable.localizedMessage.orEmpty())
                    }
                }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        if (jobFact?.isActive == true) jobFact?.cancel()
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}