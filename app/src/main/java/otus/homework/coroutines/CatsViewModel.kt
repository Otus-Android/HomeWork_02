package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val showToast: (String) -> Unit
) : ViewModel() {
    private val _state = MutableStateFlow(Model.EMPTY_MODEL)
    val state = _state.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(
                (coroutineContext[CoroutineName]?.name + throwable.message)
            )
            showToast.invoke(throwable.message.toString())
        }

    fun getContent() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val factDeferred =
                async(CoroutineName("coroutine getCatFact")) { catsService.getCatFact() }
            val imageDeferred =
                async(CoroutineName("coroutine getImage")) { imageService.getImage().first() }

            val fact = factDeferred.await()
            val image = imageDeferred.await()

            _state.value = Model(fact = fact, imageUrl = image.url)
        }
    }
}

class ViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val showToast: (String) -> Unit
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T = CatsViewModel(catsService, imageService, showToast) as T
}

//sealed class Result {
//    data class Success<T>(val data: T) : Result()
//    data class Error(val msg: String) : Result()
//}