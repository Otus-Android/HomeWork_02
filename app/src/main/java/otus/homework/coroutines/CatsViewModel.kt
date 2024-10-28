package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val pictureService: PicturesService,
) : ViewModel() {

    private val _viewState = MutableLiveData<Result>()
    val viewState: LiveData<Result> get() = _viewState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _viewState.value = Result.Error(throwable.message ?: "Неизвестная ошибка")
    }

    init {
        getCatInfo()
    }

    fun getCatInfo() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val fact = async { catsService.getCatFact() }
                val pictures = async { pictureService.getPictures() }
                _viewState.postValue(
                    Result.Success(
                        CatsInfo(
                            fact.await().fact, pictures.await().firstOrNull()?.url.orEmpty()
                        )
                    )
                )
            } catch (e: Exception) {
                _viewState.value = Result.Error("Не удалось получить ответ от сервера")
                CrashMonitor.trackWarning()
            }
        }
    }
}

sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val error: String) : Result()
}
