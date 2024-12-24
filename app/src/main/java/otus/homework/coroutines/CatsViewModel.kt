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


    private val mutCatsLiveData: MutableLiveData<Result> =MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = mutCatsLiveData

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
                mutCatsLiveData.value = Error(timeoutError)
            } else {
                mutCatsLiveData.value = Error(exception.message)
            }}

        viewModelScope.launch(handler) {
            val fact = factJob.await()
            val image = imageJob.await()
            mutCatsLiveData.value = Success(CatData(fact,image.first()))
        }

    }

}