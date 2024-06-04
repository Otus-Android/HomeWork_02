package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _liveData = MutableLiveData<ApiResult<Content>>()
    val liveData: LiveData<ApiResult<Content>> = _liveData

    val context = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable.message.toString())
    }  + SupervisorJob()

    init {
        populate()
    }

    fun populate() {
        viewModelScope.launch(context) {
            val resultFact = async { catsService.getCatFact() }
            val resultImg = async { catsService.getImg() }
            try {
                val content = Content(resultImg.await(), resultFact.await())
                _liveData.value = ApiResult.Success(content)
            } catch (_: CancellationException) {
            }catch (e: SocketTimeoutException) {
                _liveData.value = ApiResult.Error(Throwable("Не удалось получить ответ от сервером"))
            } catch (e: Exception) {
                _liveData.value = ApiResult.Error(Throwable(e.message))
            }
        }
    }
}