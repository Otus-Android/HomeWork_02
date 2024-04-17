package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
): ViewModel() {
    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = viewModelScope.launch {
            try {
                val fact = catsService.getCatFact()
                val image = imageService.getRandomImage()
                val model = FactPresentationModel(fact, image.first())
                withContext(Dispatchers.Main) {
                    _catsView?.populate(model)
                }
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
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
        job?.cancel()
        _catsView = null
    }
}