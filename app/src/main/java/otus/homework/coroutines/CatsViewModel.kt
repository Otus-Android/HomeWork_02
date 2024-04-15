package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
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
                _state.value = Result.Success(
                    CatsFactModel(
                        fact = catsService.getCatFact().fact,
                        image = catsService.getImage().first().url
                    )
                )
            } catch (e: SocketTimeoutException) {
                _state.value = Result.Error
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        job = null
        super.onCleared()
    }
}