package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                coroutineScope {
                    val fact = async { catFactService.getCatFact() }
                    val image = async { catImageService.getCatImage().first() }
                    val cat = Cat(fact.await(), image.await())
                    _catsView?.populate(cat)
                }
            } catch (t: Throwable) {
                when (t) {
                    is CancellationException -> throw t
                    is SocketTimeoutException -> _catsView?.showToast("Не удалось получить ответ от сервера")
                    else -> {
                        _catsView?.showToast(t.message ?: "Неизвестная ошибка")
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
        _catsView = null
        presenterScope.cancel()
    }
}