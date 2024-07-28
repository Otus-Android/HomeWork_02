package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.Deferred
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageLinkService: CatsImageLinkService
) {

    private var _catsView: ICatsView? = null
    protected val job = Job()
    protected val scope = CoroutineScope(Dispatchers.Main+job+CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        scope.launch {
            try {
                val fact: Deferred<Fact> = async { catsService.getCatFact() }
                val imgUrl: Deferred<CatsImageLink> = async { catsImageLinkService.getCatImageLink().first() }
                _catsView?.populate(CatsViewData(fact.await().fact,imgUrl.await().url))
            }
            catch(error :java.net.SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервера")
            }
            catch( error : Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showError(error.message?:"Ошибка")
                if(error is CancellationException)
                    throw error
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancel()
        _catsView = null
    }
}