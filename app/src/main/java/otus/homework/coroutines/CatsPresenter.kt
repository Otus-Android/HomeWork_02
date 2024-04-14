package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val onErrorMessage: (String) -> Unit
) {

    private var _catsView: ICatsView? = null
    private var getFactJob: Job? = null

    fun onInitComplete() {
        getFactJob = PresenterScope().launch {
            runCatching { catsService.getCatFact() }
                .onSuccess { _catsView?.populate(it) }
                .onFailure {
                    if (it is SocketTimeoutException) {
                        onErrorMessage(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
                        return@launch
                    }

                    val message = it.localizedMessage.orEmpty()
                    onErrorMessage(message)
                    CrashMonitor.trackWarning(message)
                }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null

    }

    fun canselJob() {
        if (getFactJob?.isActive == true) getFactJob?.cancel()
    }

    private companion object {
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
    }
}

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")

}