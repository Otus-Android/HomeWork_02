package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactsService: CatFactsService,
    private val catImageService: CatImageService
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val catFact = async { catFactsService.getCatFact() }
                val catImage = async { catImageService.getCatImages().first() }
                _catsView?.populate(Cat(catFact.await(),catImage.await()))
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> return@launch
                    is SocketTimeoutException -> _catsView?.showTimeoutError()
                    else -> _catsView?.showError(e.message)
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
        job = null
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

}