package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private val timeoutError: String = "Не удалось получить ответ от сервером"

    fun onInitComplete() {

            val factJob = presenterScope.async{
                catsService.getCatFact()
            }
            val imageJob = presenterScope.async{
                imageService.getCatImage()
             }

            presenterScope.launch {
                try {
                    val fact = factJob.await()
                    val image = imageJob.await()
                    _catsView?.populate(CatData(fact,image.first()))

                }
                catch (t: Throwable) {
                    when (t) {
                        is CancellationException -> throw t
                        is SocketTimeoutException -> _catsView?.showToast(timeoutError)
                        else -> {
                            _catsView?.showToast(t.message)
                            CrashMonitor.trackWarning(t.message)
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
        presenterScope.coroutineContext.cancelChildren()
    }

}