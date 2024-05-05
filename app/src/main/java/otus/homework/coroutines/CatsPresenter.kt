package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob + CoroutineName("CatsCoroutine") )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catImageService.getCatImage() }

                val factResponse = factDeferred.await()
                val imageResponse = imageDeferred.await()

                if (factResponse.isSuccessful) {
                    val fact = factResponse.body()?.fact ?: ""
                    val image = imageResponse[0].url

                    _catsView?.populate(CatInfo(fact, image))
                } else {
                    CrashMonitor.trackWarning()
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервером")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showError(e.message ?: "Произошла ошибка")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterJob.cancel()
    }
}