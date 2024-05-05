package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
  private val catsRepository: ICatsRepository,
): ViewModel() {
  private var _catsJob: Job? = null

  private val mutableUiState = MutableLiveData<Result<Cat>>()
  val uiState: LiveData<Result<Cat>> = mutableUiState

  private val exceptionHandle = CoroutineExceptionHandler { _, throwable ->
    when (throwable) {
      is SocketTimeoutException -> {
        mutableUiState.value = Error("Не удалось получить ответ от сервера")
      }
      else -> {
        throwable.printStackTrace()
        CrashMonitor.trackWarning()
        throwable.message?.let { eMessage ->
          mutableUiState.value = Error(eMessage)
        }
      }
    }
  }

  fun onInitComplete() {
    cancelCatJob()
    _catsJob = viewModelScope.launch(exceptionHandle) {
      val cat: Cat = loadCat()

      mutableUiState.value = Success(cat)
    }
  }

  private suspend fun loadCat(): Cat = coroutineScope {
    val defFact = async {
      catsRepository.getCatFact()
    }
    val defPresentation = async {
      catsRepository.getCatPresentation()
    }
    val (fact, presentation) = Pair(defFact.await(), defPresentation.await())

    Cat(
      fact = fact,
      presentation = presentation,
    )
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