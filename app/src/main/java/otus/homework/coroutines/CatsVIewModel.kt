package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.api.CatsService
import otus.homework.coroutines.data.api.PicturesService
import otus.homework.coroutines.data.model.CatsInfo
import otus.homework.coroutines.domain.ICatsPresenter
import otus.homework.coroutines.domain.Result


class CatsViewModel(
    private val catsService: CatsService,
    private val catImageService: PicturesService
) : ViewModel(), ICatsPresenter {
    private var _catsView: ICatsView? = null
    private var workJob: Job? = null
    private val _viewState = MutableSharedFlow<Result>(extraBufferCapacity = 1)
    val viewState: SharedFlow<Result> get() = _viewState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning("error is now")
        _viewState.tryEmit(otus.homework.coroutines.domain.Result.Error(
            throwable.message ?: "Неизвестная ошибка")
        )
    }


    override fun onInitComplete() {
        workJob = viewModelScope.launch(exceptionHandler) {
            try {
                val fact = async { catsService.getCatFact() }
                val pictures = async { catImageService.getPictures() }
                val data = CatsInfo(
                    fact = fact.await().fact,
                    picture = pictures.await().firstOrNull()?.url.orEmpty(),
                )
                //variant one
                _viewState.emit(
                    Result.Success(
                        data
                    )
                )
                // variant 2
                _catsView?.populate(data)
            } catch (e: Exception) {
                _viewState.emit(Result.Error("Smth get wrong "))
                CrashMonitor.trackWarning("smth get wrong")
            }
        }
    }

    override fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun detachView() {
        _catsView = null
    }
}