package otus.homework.coroutines

import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        try {
            val responseCatFact = catsService.getCatFact()
            if (responseCatFact.isSuccessful && responseCatFact.body() != null) {
                _catsView?.populate(responseCatFact.body()!!)
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