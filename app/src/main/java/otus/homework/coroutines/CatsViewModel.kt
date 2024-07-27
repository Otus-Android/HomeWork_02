package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService,
) : ViewModel() {

    private val _resultLiveData = MutableLiveData<Result<CatModel>>()
    val resultLiveData: LiveData<Result<CatModel>> = _resultLiveData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val error = if (exception is java.net.SocketTimeoutException) {
            Throwable("Не удалось получить ответ от сервером")
        } else {
            exception
        }
        _resultLiveData.value = Result.Error(error)
        CrashMonitor.trackWarning(error.message)
    }

    fun populateData() = viewModelScope.launch(coroutineExceptionHandler) {
        val fact = async {
            val response = catsService.getCatFact()
            Log.d("FACT RESPONSE", response.toString())
            response.body()!!
        }
        val image = async {
            val response = imageService.getRandomImage()
            Log.d("IMAGE RESPONSE", response.toString())
            response.body()!!
        }
        val cat = CatModel(fact.await().fact, image.await()[0].url)
        Log.d("CAT", cat.toString())
        _resultLiveData.value = Result.Success(cat)
    }
}