package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val apiService: ApiService,
    private val onShowErrorMessage: (Throwable) -> Unit
) {
    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = CoroutineScope(
            Dispatchers.Main + CoroutineName(COROUTINE_NAME)
        ).launch {
            val factResult = async {
                runCatching {
                    apiService.serviceCatFact.getCatFact().fact
                }.onFailure { showError(it) }
            }
            val imageResult = async {
                runCatching {
                    apiService.serviceCatImage.getCatImage().firstOrNull()?.url.orEmpty()
                }.onFailure { showError(it) }
            }

            _catsView?.populate(
                CatData(
                    fact = factResult.await().getOrNull().orEmpty(),
                    imageUrl = imageResult.await().getOrNull().orEmpty()
                )
            )
        }
    }

    private fun showError(throwable: Throwable) {
        onShowErrorMessage(throwable)
        if (throwable is SocketTimeoutException) return

        CrashMonitor.trackWarning(throwable.localizedMessage.orEmpty())
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        if (job?.isActive == true) job?.cancel()
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}