package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        try {
            val catFact = catsFactService.getCatFact()
            val сatImages = catsImageService.getCatImage()

            if (catFact != null && сatImages != null) {
                _catsView?.populate(catFact)
                _catsView?.renderingImage(сatImages)
            } else {
                _catsView?.showToast("Нет данных")
            }
        } catch (exception: SocketTimeoutException) {
            _catsView?.showToast("Не удалось получить ответ от сервера")
        } catch (exception: Throwable) {
            CrashMonitor.trackWarning()
            _catsView?.showToast("${exception.message}")
            throw CancellationException(exception.message)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}