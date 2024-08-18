package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

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
            try {
                val fact = catsService.getCatFact()
                val image = imageService.getCatImage()

                val factImage = FactImage(
                    fact = fact.fact,
                    url = image.first().url
                )
                _catsView?.populate(Result.Success(factImage))
            } catch (e: SocketTimeoutException) {
                _catsView?.populate(Result.Error("Не удалось получить ответ от сервером"))
            } catch (e1: Exception) {
                _catsView?.populate(Result.Error(e1.message ?: ""))
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