package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _state = MutableLiveData<Result>()
    val state: LiveData<Result> = _state

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    init {
        loadFact()
    }


    fun loadFact() {
        job?.cancel()
        job = viewModelScope.launch(exceptionHandler) {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsService.getImage() }
                _state.value = Result.Success(
                    CatsFactModel(
                        fact = factDeferred.await().fact,
                        image = imageDeferred.await().first().url
                    )
                )
            } catch (e: SocketTimeoutException) {
                _state.value = Result.Error
            }
        }
    }
}