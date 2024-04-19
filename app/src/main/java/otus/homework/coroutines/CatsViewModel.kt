package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
) : ViewModel() {
    private val _catsData = MutableLiveData<Result<CatFactAndImage>>()
    val catsData: LiveData<Result<CatFactAndImage>> = _catsData

    private val handler = CoroutineExceptionHandler { _, exception ->
        _catsData.postValue(Result.Error("An error occurred: ${exception.localizedMessage}"))
        exception.localizedMessage?.let { CrashMonitor.trackWarning(it) }
    }

    fun loadCatData() {
        viewModelScope.launch(handler) {
            val fact = async { catsService.getCatFact() }
            val image = async { catsService.getCatImage().firstOrNull() }
            _catsData.postValue(Result.Success(CatFactAndImage(fact.await(), image.await()?.url)))
        }
    }
}