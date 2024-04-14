package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val onErrorMessage: (String) -> Unit
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = PresenterScope().launch {
            val factResult = async {
                runCatching { catsService.getCatFact().fact }
                    .onFailure { showError(it) }
            }
            val imageResult = async {
                runCatching { catsService.getImage().firstOrNull()?.url.orEmpty() }
                    .onFailure { showError(it) }
            }

            _catsView?.populate(
                SomeCatFact(
                    factText = factResult.await().getOrNull().orEmpty(),
                    imageUrl = imageResult.await().getOrNull().orEmpty()
                )
            )
        }
    }

    private fun showError(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            onErrorMessage(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
            return
        }

        val message = throwable.localizedMessage.orEmpty()
        onErrorMessage(message)
        CrashMonitor.trackWarning(message)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null

    }

    fun canselJob() {
        if (job?.isActive == true) job?.cancel()
    }

    private companion object {
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
    }
}

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")

}