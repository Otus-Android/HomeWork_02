package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsFacstService,
    private val imageService: CatsImagesService
): ViewModel() {

    private var _catsView: ICatsView? = null

    private val presenterScope: CoroutineScope = CoroutineScope(
    Dispatchers.Main
    + SupervisorJob()
    + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.populate(getContent())
            } catch (error: Throwable){
                handleError(error)
            }
        }
    }

    private suspend fun getContent(): CatsContent =
        coroutineScope {
            val fact = async {
                catsService.getCatFact()
            }
            val image = async {
                imageService.getImage()
            }
            CatsContent(fact.await(), image.await().first())
        }




    private fun handleError(error: Throwable){
        if(error is java.net.SocketTimeoutException){
            _catsView?.showToast(ERROR_MESSAGE)
        } else if(error !is CancellationException) {
            CrashMonitor.trackWarning()
            _catsView?.showToast(error.message.toString())
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }



    private companion object{
        const val ERROR_MESSAGE = "Не удалось получить ответ от серверa"
    }
}