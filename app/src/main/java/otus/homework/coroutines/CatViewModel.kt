package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatViewModel(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService,
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private val _liveData = MutableLiveData<Result>()
    val liveData = _liveData

    fun onInitComplete() {
        PresenterScope.scope.launch(PresenterScope.exceptionHandler) {
            try {
                val success = Result.Success(
                    CatModel(
                        catsFactService.getCatFact().fact,
                        catsImageService.getCatImage().first().url
                    )
                )

                _liveData.postValue(success)
            } catch (e: SocketTimeoutException) {
                _catsView?.toast("Не удалось получить ответ от сервером")
            } catch (e: Throwable) {
                _catsView?.toast(e.message)
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

object PresenterScope {
    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
    )
}
