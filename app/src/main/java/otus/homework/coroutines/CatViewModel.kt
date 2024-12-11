package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatViewModel(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService,
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private val _liveData = MutableLiveData<Result>()
    val liveData = _liveData

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            try {
                _liveData.postValue(
                    Result.Success(
                        CatModel(
                            catsFactService.getCatFact().fact,
                            catsImageService.getCatImage().first().url
                        )
                    )
                )
            } catch (e: SocketTimeoutException) {
                _liveData.postValue(Result.Error("Не удалось получить ответ от сервером"))
            } catch (e: Throwable) {
                _liveData.postValue(Result.Error(e.message))
                throw e
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
