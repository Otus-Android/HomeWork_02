package otus.homework.coroutines

import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        try {
            val responseCatFact = catsFactService.getCatFact()
            if (responseCatFact.isSuccessful && responseCatFact.body() != null) {
                val responseCatImage = catsImageService.getCatImage()
                if (responseCatImage.isSuccessful && responseCatImage.body() != null) {
                    _catsView?.populate(responseCatFact.body()!!)
                    _catsView?.renderingImage(responseCatImage.body()!!)
                }
            }
        } catch (exception: SocketTimeoutException) {
            _catsView?.showToast("Не удалось получить ответ от сервера")
        } catch (exception: Throwable) {
            CrashMonitor.trackWarning()
            _catsView?.showToast("${exception.message}")
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}