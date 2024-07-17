package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    private var _catsView: ICatsView? = null

    private val presenterScope =
        CoroutineScope(Job() + Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val pictures = async { catsService.getRandomPictures() }

                _catsView?.populate(
                    catInfo = CatInfo(
                        fact = fact.await(),
                        url = pictures.await().firstOrNull()
                    )
                )

            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервером")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(e.message.toString())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelScope() {
        presenterScope.cancel()
    }
}