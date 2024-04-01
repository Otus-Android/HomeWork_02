package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.Result
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImage: CatsImage,
) : ViewModel() {
    private val _catData = MutableStateFlow<Result<CatData>>(Result.Default)
    val catData = _catData as StateFlow<Result<CatData>>

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            if (throwable is SocketTimeoutException)
                _catData.value = Result.Error("Не удалось получить ответ от сервера")
            else {
                CrashMonitor.trackWarning()
                _catData.value = Result.Error(throwable.message)
            }
        }
    }

    fun getCatData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                val catData = async {
                    CatData(
                        catsService.getCatFact(),
                        catsImage.getCatImage().firstOrNull()
                    )
                }
                _catData.value = Result.Success(catData.await())
            }.onFailure { throwable ->
                CrashMonitor.trackWarning()
                _catData.value = Result.Error(throwable.message)
            }
        }

    }
}