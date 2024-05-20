package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.model.ViewData
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _catsData: MutableLiveData<Result?> = MutableLiveData()
    val catsData: LiveData<Result?>
        get() = _catsData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val fact = async { catsService.getCatFact() }
            val pic = async { catsService.getCatPic() }
            _catsData.value = Result.Success(
                ViewData(
                    fact = fact.await(),
                    pic = pic.await().firstOrNull()
                )
            )
        } catch (e: SocketTimeoutException) {
            _catsData.value = Result.Error("Не удалось получить ответ от сервера")
        } catch (e: Exception) {
            CrashMonitor.trackWarning()
            _catsData.value = Result.Error(e.message.orEmpty())
        }
    }

    override fun onCleared() {
        super.onCleared()
        _catsData.value = null
    }
}