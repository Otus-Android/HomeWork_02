package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
): ViewModel() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)

            } catch (error: Throwable) {
                handleError(error)
            }
        }
    }

    private fun handleError(error: Throwable){
        if(error is java.net.SocketTimeoutException){
            _catsView?.showToast(ERROR_MESSAGE)
        } else {
            CrashMonitor.trackWarning()
            _catsView?.showToast(error.message.toString())
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.close()
    }

    private val presenterScope: CloseableCoroutineScope
        get()  {
            var scope: CloseableCoroutineScope? = null
            return if (scope != null) {
                scope
            } else {
                scope = CloseableCoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
                scope
            }
        }

    /**
     * Интерфейс описывающий закрываемый скоуп
     */
    private class CloseableCoroutineScope(override val coroutineContext: CoroutineContext): CoroutineScope {

        /**
         * Отменяем Job связанный со скоупом через корутин контекст
         */
        fun close() = coroutineContext.cancel()
    }

    private companion object{
        const val ERROR_MESSAGE = "Не удалось получить ответ от серверa"
    }
}