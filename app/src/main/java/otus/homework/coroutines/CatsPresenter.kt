package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsServiceImage: CatsServiceImage
) {

    private val tag = "CP"
    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main
            + CoroutineName("CatsCoroutine") + SupervisorJob())

    fun onInitComplete() {
        val factDef = presenterScope.async { catsService.getCatFact() }
        val imageDef = presenterScope.async { catsServiceImage.getCatImage() }
        presenterScope.launch {
            var fact = Fact("", 0)
            var image = ""
            try {
                fact = factDef.await()
                Log.d(tag, "fact was received: $fact")
            } catch (e: Throwable) {
                if (e is SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")
                } else {
                    CrashMonitor.trackWarning(e)
                    _catsView?.showToast(e.message)
                }
            }
            try {
                val catImage = imageDef.await()
                Log.d(tag, "image was received: " + catImage[0].toString())
                image = catImage[0].url
            } catch (e: Throwable) {
                if (e is SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")
                } else {
                    CrashMonitor.trackWarning(e)
                    _catsView?.showToast(e.message)
                }
            }
            _catsView?.populate(CatsModel(fact, image))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}