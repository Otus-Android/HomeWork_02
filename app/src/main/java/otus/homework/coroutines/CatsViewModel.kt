package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {
    private var _catsView: ICatsView? = null

    private val catsExceptionhandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> {
                _catsView?.showToast("Не удалось получить ответ от сервером")
            }

            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showToast(exception.message.toString())
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(catsExceptionhandler) {
            val fact = async { catsService.getCatFact() }
            val pictures = async { catsService.getRandomPictures() }

            _catsView?.populate(
                catInfo = CatInfo(
                    fact = fact.await(),
                    url = pictures.await().firstOrNull()
                )
            )
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}