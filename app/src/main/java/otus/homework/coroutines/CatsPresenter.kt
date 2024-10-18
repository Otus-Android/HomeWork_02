package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private var catsJob: Job? = null

    private val _fact = MutableSharedFlow<String?>()
    private val _imageUrl = MutableSharedFlow<String?>()

    private val _eventShowErrorConnectToServer = MutableSharedFlow<Unit>()
    val eventShowErrorConnectToServer = _eventShowErrorConnectToServer.asSharedFlow()

    private val _eventShowExceptionMessage = MutableSharedFlow<String>()
    val eventShowExceptionMessage = _eventShowExceptionMessage.asSharedFlow()

    fun initCatStateListener() {
        PresenterScope(Dispatchers.Default).launch {
            combine(_fact, _imageUrl) { fact, imageUrl ->
                withContext(Dispatchers.Main) {
                    _catsView?.populate(Cat(fact, imageUrl))
                }
            }
                .collect()
        }
    }

    fun onInitComplete() {
        catsJob?.cancel()
        catsJob = PresenterScope(Dispatchers.IO).launch {
            runCatching { catsService.getCatFact() }
                .fold(
                    onSuccess = { result -> _fact.emit(result.body()?.fact) },
                    onFailure = { error -> handleError(error) }
                )
            runCatching { imageService.getRandomImages() }
                .fold(
                    onSuccess = { result -> _imageUrl.emit(result.body()?.first()?.url) },
                    onFailure = { error -> handleError(error) }
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

    private suspend fun handleError(error: Throwable) {
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
}