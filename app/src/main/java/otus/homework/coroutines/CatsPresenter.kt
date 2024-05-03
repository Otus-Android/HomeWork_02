package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService
) {
    private var job: Job? = null  // Создание Job для управления жизненным циклом корутин
    private var _catsView: ICatsView? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        job = coroutineScope.launch {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsService.getCatImage().firstOrNull() }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                _catsView?.populate(CatFactAndImage(fact, image?.url))
            } catch (e: Throwable) {
                handleErrors(e)
            }
        }
    }

    private fun handleErrors(e: Throwable) {
        when (e) {
            is CancellationException -> throw e  // Проброс CancellationException дальше
            is SocketTimeoutException -> {
                _catsView?.showError("Не удалось получить ответ от сервера")
            }

            else -> {
            _catsView?.showError(e.message.orEmpty())
                CrashMonitor.trackWarning(e.message.orEmpty())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }


    fun detachView() {
        _catsView = null

    }

    fun onDestroy() {
        coroutineScope.cancel()  // Отмена всех корутин
    }
}