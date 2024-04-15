package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private var presenterJob: Job? = null
    private val presenterScope = CoroutineScope(
        context = Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterJob = presenterScope.launch {
            try {
                _catsView?.populate(getFact()!!)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast(ExceptionType.SocketTimeout)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                if (e.message != null ) _catsView?.showToast(ExceptionType.Other(e.message!!))
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.launch {
            presenterJob?.cancel()
        }
        _catsView = null
    }

    private suspend fun getFact(): Fact? {
        var fact: Fact?
        withContext(Dispatchers.IO) {
            fact = catsService.getCatFact()
        }
        return fact
    }
}

sealed class ExceptionType {
    object SocketTimeout : ExceptionType()
    data class Other(val message: String): ExceptionType()
}
