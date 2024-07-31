package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val apiService: ApiService,
    private val onShowErrorMessage: (Throwable) -> Unit
) : ViewModel(), ICatsPresenter {

    override var _catsView: ICatsView? = null
    override var job: Job? = null

    override fun onInitComplete() {
        viewModelScope.launch {
            try {
                val factResult = async { apiService.serviceCatFact.getCatFact().fact }
                val imageResult =
                    async { apiService.serviceCatImage.getCatImage().firstOrNull()?.url.orEmpty() }
                val catData =
                    CatData(
                        fact = factResult.await(),
                        imageUrl = imageResult.await()
                    )

                _catsView?.populate(Result.Success(catData))
            }
            catch (e : Exception){
                showError(e)
            }
        }
    }

    private fun showError(throwable: Throwable) {
        onShowErrorMessage(throwable)

        if (throwable is SocketTimeoutException) return

        CrashMonitor.trackWarning(throwable.localizedMessage.orEmpty())
    }
}