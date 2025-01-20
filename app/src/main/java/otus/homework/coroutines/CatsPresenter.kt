package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException


class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val presenterScope: CoroutineScope
) {
    private var _catsView: ICatsView? = null
    private var job: Job? = null

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            if (throwable is SocketTimeoutException) {
                showToast("Не удалось получить ответ от сервером")
            } else {
                showToast(throwable.message ?: "No message")
                CrashMonitor.trackWarning(throwable)
            }
        }
    }

    fun onInitComplete() {
        job?.cancel()
        job = presenterScope.launch(exceptionHandler) {
            val factDeferred = async { catsService.getCatFact().body()?.fact }
            val imageDeferred = async { imageService.getImage().body()?.getOrNull(0)?.url }

            val fact = factDeferred.await()
            val image = imageDeferred.await()

            if (!fact.isNullOrEmpty() && !image.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    _catsView?.populate(
                        DataModel(
                            fact.toString(),
                            image
                        )
                    )
                }
            }
        }
    }

    private fun showToast(text: String) {
        presenterScope.launch(Dispatchers.Main) {
            _catsView?.showToast(text)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        job?.cancel()
    }
}