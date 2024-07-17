package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsService,
    private val catsPicturesService: CatsPicturesService,
    private val connectionErrorHandler: ConnectionErrorHandler
) {

    private var _catsView: ICatsView? = null
    private val presenterScope: PresenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                fetchAndUpdateCatInfo()
            } catch (e: SocketTimeoutException) {
                connectionErrorHandler.onError()
            } catch(e: Exception) {
                CrashMonitor.trackWarning(e.message)
            }
        }
    }

    private suspend fun fetchAndUpdateCatInfo() {
        val catData = loadCatData()
        _catsView?.populate(catData)
    }

    private suspend fun loadCatData(): CatData {
        val factRequest = presenterScope.async {
            catsFactService.getCatFact()
        }
        val imageRequest = presenterScope.async {
            catsPicturesService.getCatPicture()
        }

        val fact = factRequest.await()
        val picture = imageRequest.await()

        return CatData(fact.fact, picture.first().url)
    }

    fun cancelFetch() = presenterScope.cancel()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}