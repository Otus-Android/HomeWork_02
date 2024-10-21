package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catFactsService: CatFactsService,
    private val imagesService: ImagesService
): ViewModel() {

    private val _catFactFlow = MutableStateFlow<CatFactResult>(CatFactResult.NoFact)
    val catFactResultFlow: StateFlow<CatFactResult> = _catFactFlow.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, t ->
        when (t) {
            is SocketTimeoutException -> _catFactFlow.value = CatFactResult.SocketTimeoutError
            else -> {
                _catFactFlow.value = CatFactResult.Error(t.message)
                CrashMonitor.trackError(t)
            }
        }
    }

    fun getCatFact() {
        viewModelScope.launch(errorHandler) {
            val fact = async { catFactsService.getCatFact() }
            val image = async { imagesService.getImages().first() }
            val catFact = CatFact(fact.await(), image.await())

            _catFactFlow.value = CatFactResult.Success(catFact)
        }
    }
}