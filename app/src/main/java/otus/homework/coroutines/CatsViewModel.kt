package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
  private val catsRepository: ICatsRepository,
): ViewModel() {
  private var _catsJob: Job? = null

  val uiState = MutableLiveData<Result<Cat>>()

  private val exceptionHandle = CoroutineExceptionHandler { _, throwable ->
    when (throwable) {
      is CancellationException -> {
        // ...
      }
      is SocketTimeoutException -> {
        uiState.value = Error("Не удалось получить ответ от сервера")
      }
      else -> {
        throwable.printStackTrace()
        CrashMonitor.trackWarning()
        throwable.message?.let { eMessage ->
          uiState.value = Error(eMessage)
        }
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    cancelCatJob()
  }

  fun onInitComplete() {
    cancelCatJob()
    _catsJob = viewModelScope.launch(exceptionHandle) {
      val cat: Cat = catsRepository.getCat()
      uiState.value = Success(cat)
    }
  }

  private fun cancelCatJob() {
    _catsJob?.cancel()
    _catsJob = null
  }

  class Factory(
    private val catsRepository: ICatsRepository,
  ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return CatsViewModel(
        catsRepository = catsRepository,
      ) as T
    }
  }
}