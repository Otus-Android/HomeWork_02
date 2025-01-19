package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException


class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: CoroutineScope
) {
    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job?.cancel()
        job = presenterScope.launch {
            runCatching { catsService.getCatFact() }
                .onSuccess { response ->
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) { _catsView?.populate(response.body()!!) }
                    }
                }
                .onFailure { t ->
                    if (t is SocketTimeoutException) {
                        withContext(Dispatchers.Main) { _catsView?.showToast("Не удалось получить ответ от сервером") }
                    } else {
                        withContext(Dispatchers.Main) {
                            _catsView?.showToast(
                                t.message ?: "No message"
                            )
                        }
                        CrashMonitor.trackWarning(t)
                    }
                }
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