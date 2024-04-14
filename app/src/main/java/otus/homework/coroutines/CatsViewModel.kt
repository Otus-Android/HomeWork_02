package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        val handler = CoroutineExceptionHandler { _, throwable -> showError(throwable) }

        viewModelScope.launch(handler) {
            val factResult = async { catsService.getCatFact().fact }
            val imageResult = async { catsService.getImage().firstOrNull()?.url.orEmpty() }
            val someCatFact =
                SomeCatFact(factText = factResult.await(), imageUrl = imageResult.await())

            _catsView?.populate(Result.Success(someCatFact))
        }
    }

    private fun showError(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            _catsView?.populate(Result.Error(SOCKET_TIMEOUT_EXCEPTION_MESSAGE))
            return
        }

        val message = throwable.localizedMessage.orEmpty()
        _catsView?.populate(Result.Error(message))
        CrashMonitor.trackWarning(message)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    private companion object {
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
    }
}