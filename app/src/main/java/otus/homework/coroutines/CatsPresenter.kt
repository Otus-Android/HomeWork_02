package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsServiceImage: CatsServiceImage
) {

    private val tag = "CP"
    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main
            + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDef = async { catsService.getCatFact() }
                val imageDef = async { catsServiceImage.getCatImage() }
                val fact = factDef.await()
                Log.d(tag, "fact was received: $fact")
                val catImage = imageDef.await()
                Log.d(tag, "image was received: " + catImage[0].toString())
                val image = catImage[0].url
                _catsView?.populate(CatsModel(fact, image))
            } catch (e: Throwable) {
                when(e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException ->
                        _catsView?.showToast("Не удалось получить ответ от сервера")
                    else -> {
                        CrashMonitor.trackWarning(e)
                        _catsView?.showToast(e.message)
                    }
                }
            }
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