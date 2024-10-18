package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private var catsJob: Job? = null

    private val _eventShowErrorConnectToServer = MutableSharedFlow<Unit>()
    val eventShowErrorConnectToServer = _eventShowErrorConnectToServer.asSharedFlow()

    private val _eventShowExceptionMessage = MutableSharedFlow<String>()
    val eventShowExceptionMessage = _eventShowExceptionMessage.asSharedFlow()

    fun onInitComplete() {
        catsJob?.cancel()
        catsJob = PresenterScope(Dispatchers.IO).launch {
            runCatching { catsService.getCatFact() }
                .fold(
                    onSuccess = { result ->
                        withContext(Dispatchers.Main) {
                            result.body()?.let { _catsView?.populate(it) }
                        }
                    },
                    onFailure = { error ->
                        when (error) {
                            is java.net.SocketTimeoutException -> {
                                _eventShowErrorConnectToServer.emit(Unit)
                            }

                            else -> {
                                CrashMonitor.trackWarning(error)
                                error.message?.let { _eventShowExceptionMessage.emit(it) }
                            }
                        }
                    }
                )
        }
    }

    fun cancelCatsJob() = catsJob?.cancel()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}