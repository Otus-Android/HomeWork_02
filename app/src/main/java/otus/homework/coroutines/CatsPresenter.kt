package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsPresenter(
    private val catsService : CatsService,
) {

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            val fact = async(Dispatchers.IO) {
                try {
                    catsService.getCatFact()
                } catch (e : SocketTimeoutException) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    }
                    throw CancellationException()
                } catch (e : Exception) {
                    CrashMonitor.trackWarning()
                    withContext(Dispatchers.Main) {
                        _catsView?.showToast(e.message ?: "Uknown exception")
                    }
                    throw CancellationException()
                }
            }

            _catsView?.populate(fact.await())
        }
    }

    fun attachView(catsView : ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}