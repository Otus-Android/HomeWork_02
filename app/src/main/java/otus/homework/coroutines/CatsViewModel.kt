package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsServiceImage: CatsServiceImage
) : ViewModel() {

    private val tag = "CVM"
    private val handler = CoroutineExceptionHandler { _, e ->
        val error = if (e is SocketTimeoutException) {
            "Не удалось получить ответ от сервера"
        } else {
            e.message.toString()
        }
        CrashMonitor.trackWarning(error)
        _catsLiveData.value = Error(error)
    }
    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            val factDef = async { catsService.getCatFact() }
            val imageDef = async { catsServiceImage.getCatImage() }
            val fact = factDef.await()
            Log.d(tag, "fact was received: $fact")
            val catImage = imageDef.await()
            Log.d(tag, "image was received: " + catImage[0].toString())
            _catsLiveData.value = Success(CatsModel(fact, catImage[0].url))
        }
    }
}