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
    private val catFactsService: CatFactsService,
    private val imagesService: ImagesService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                coroutineScope {
                    val catFact = async { catFactsService.getCatFact() }
                    val image = async { imagesService.getImages().first() }

                    _catsView?.populate(catFact.await(), image.await())
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showSocketTimeoutError()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(e.message)
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