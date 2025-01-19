package otus.homework.coroutines.domain

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.data.api.CatsService
import otus.homework.coroutines.data.api.PicturesService
import otus.homework.coroutines.data.model.CatsInfo
import retrofit2.awaitResponse
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val pictureService: PicturesService

):ICatsPresenter{
    private var _catsView: ICatsView? = null
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

   override fun onInitComplete() {
        getCatInfo()
    }

    override fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

   override fun detachView() {
        _catsView = null
        job?.cancel()
        job = null
    }

    private fun getCatInfo() {
        job = presenterScope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val pictures = async { pictureService.getPictures() }
                _catsView?.populate(
                    CatsInfo(
                        fact = fact.await().fact,
                        picture = pictures.await().firstOrNull()?.url.orEmpty()
                    )
                )
            } catch (e: SocketTimeoutException) {
                _catsView?.showError("Time out ")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e.message.toString())
                _catsView?.showError(e.message.toString())
            }
        }
    }

}
interface ICatsPresenter {
    fun onInitComplete()
    fun attachView(catsView: ICatsView)
    fun detachView()
}