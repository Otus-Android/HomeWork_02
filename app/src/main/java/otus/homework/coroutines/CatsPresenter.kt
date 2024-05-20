package otus.homework.coroutines

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import otus.homework.coroutines.model.ViewData
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val presenterScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    ).coroutineContext

    private var _catsView: ICatsView? = null
    private var getCatFactJob: Job? = null


    fun onInitComplete() = runBlocking {
        getCatFactJob = launch(presenterScope) {
            if (isActive) {
                try {
                    val fact = async { catsService.getCatFact() }
                    val pic = async { catsService.getCatPic() }
                    _catsView?.populate(ViewData(
                        fact = fact.await(),
                        pic = pic.await().firstOrNull()
                    ))
                } catch (e: SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")
                } catch (e: Exception) {
                    CrashMonitor.trackWarning()
                    _catsView?.showToast(e.message.orEmpty())
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        getCatFactJob?.cancel()
        presenterScope.cancel()
    }
}