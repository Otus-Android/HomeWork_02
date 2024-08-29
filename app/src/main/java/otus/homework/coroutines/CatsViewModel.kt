package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.Result
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: CatsImageService
): ViewModel() {

    private var _catsView: ICatsView? = null
    private val timeoutError: String = "Не удалось получить ответ от сервером"

    val catsLiveData: MutableLiveData<Result> =MutableLiveData<Result>()


    fun init() {

        val factJob = viewModelScope.async{
            catsService.getCatFact()
        }
        val imageJob = viewModelScope.async{
            imageService.getCatImage()
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            CrashMonitor.trackWarning(exception.message)
            if (exception is SocketTimeoutException){
                catsLiveData.value = Error(timeoutError)
            } else {
                catsLiveData.value = Error(exception.message)
            }}

        viewModelScope.launch(handler) {
            val fact = factJob.await()
            val image = imageJob.await()
            catsLiveData.value = Success(CatData(fact,image.first()))
        }

    }

}