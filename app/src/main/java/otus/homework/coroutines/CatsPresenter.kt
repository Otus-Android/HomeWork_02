package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService : CatsService,
    private val pictureService : PicturesService,
    private val catsUiStateMapper : CatsUiStateMapper,
) {

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var _catsView : ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val catsUiState = getCatsUiState()
                _catsView?.populate(catsUiState)
            } catch (e : SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервером")
                throw CancellationException()
            } catch (e : Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(e.message ?: "Uknown exception")
                throw CancellationException()
            }
        }
    }

    fun attachView(catsView : ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    private suspend fun getCatsUiState() : CatsUiState = coroutineScope {
        val fact = async { catsService.getCatFact() }
        val pictures = async { pictureService.getPictures() }

        catsUiStateMapper.map(fact.await(), pictures.await())
    }
}