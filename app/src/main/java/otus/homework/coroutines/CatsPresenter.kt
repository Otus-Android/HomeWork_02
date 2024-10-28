package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val pictureService: PicturesService
) {
    private var _catsView: ICatsView? = null
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    fun onInitComplete() {
        getCatInfo()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
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
                _catsView?.showErrorToast("Не удалось получить ответ от сервера")
                throw CancellationException()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showErrorToast(e.message ?: "Неизвестная ошибка, попробуйте позже")
                throw CancellationException()
            }
        }
    }
}