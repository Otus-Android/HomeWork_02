package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val catsCoroutineScope: CatsCoroutineScope
) {
    private val job = Job()
    private var _catsView: ICatsView? = null


    fun onInitComplete() {
        catsCoroutineScope.launch {
            try {
                val fact = catsService.getCatFact()
                val image = imageService.getRandomImage()
                val model = FactPresentationModel(fact, image.first())
                _catsView?.populate(model)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.onError(R.string.connection_error_message)
                    }
                    is CancellationException -> {
                        _catsView?.onError(R.string.connection_error_message)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancel()
        _catsView = null
    }
}