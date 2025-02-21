package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import otus.homework.coroutines.Utils
import otus.homework.coroutines.domain.CatsInteractor
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsInteractor: CatsInteractor
) : ViewModel() {
    private val job = SupervisorJob()
    private val context = job + Dispatchers.IO

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val (error, message) = when (exception) {
            is SocketTimeoutException,
            is UnknownHostException ->
                "Connection timed out" to "Не удалось получить ответ от сервера"
            else ->
                exception.message to exception.message
        }

        Utils.log { "[CatsPresenter handled error]: $error" }
        _errorHandle.postValue(message)

    }

    private val coroutineContext: CoroutineContext
        get() = context + exceptionHandler + CoroutineName("CatsCoroutine")

    private val _catFact = MutableLiveData<String?>()
    val catFact: LiveData<String?> = _catFact

    private val _catImage = MutableLiveData<RequestCreator?>()
    val catImage: LiveData<RequestCreator?> = _catImage

    private val _errorHandle = MutableLiveData<String?>()
    val errorHandle: LiveData<String?> = _errorHandle

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun updateImage() {
        viewModelScope.launch(coroutineContext) {
            catsInteractor.getCatImage().let { image ->
                if (isActive.not()) return@launch
                Utils.log { "[CatsPresenter]: get cats image" }
                _catImage.postValue(image)
            }
        }
    }

    fun updateFact() {
        viewModelScope.launch(coroutineContext) {
            catsInteractor.getCatFact().let { fact ->
                if (isActive.not()) return@launch
                Utils.log { "[CatsPresenter]: get cats fact" }
                _catFact.postValue(fact)
            }
        }
    }
}