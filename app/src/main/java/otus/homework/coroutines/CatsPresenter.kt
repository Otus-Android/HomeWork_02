package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val response = catsService.getCatFact()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _catsView?.populate(it)
                    }
                } else {
                    CrashMonitor.trackWarning()
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервером")
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
    }
}