package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService,
) : ViewModel() {
    private var _catsView: ICatsView? = null

    fun loadFact() {
        val handler = CoroutineExceptionHandler { _, error ->
            CrashMonitor.trackWarning(error.message ?: "")
        }

        viewModelScope.launch(handler) {

            val fact = async {
                catsService.getCatFact()
            }
            val image = async {
                imageService.getCatImage()
            }

            try {
                val factImage = FactImage(
                    fact = fact.await().fact,
                    url = image.await().first().url
                )
                _catsView?.populate(Result.Success(factImage))
            } catch (socketException: SocketTimeoutException) {
                _catsView?.populate(Result.Error("Не удалось получить ответ от сервером"))
            } catch (exception: Exception) {
                if (exception !is CancellationException){
                    _catsView?.populate(Result.Error(exception.message ?: ""))
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
}