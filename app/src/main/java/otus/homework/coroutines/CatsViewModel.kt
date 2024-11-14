package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _state: MutableStateFlow<Result<MainUiModel>> = MutableStateFlow(Result.Noting())
    val state = _state.asStateFlow()

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() = viewModelScope.launch(handler) {
        val text = async { getText() }
        val image = async { getImage() }

        if (text.await().isNotEmpty() && image.await().isNotEmpty())
            _state.value = Result.Success(MainUiModel(text.await(), image.await()))
    }

    private suspend fun getText(): String {
        var text = ""

        runCatching {
            catsService.getCatFact()
        }.mapCatching { response ->
            text = response.fact
        }.getOrElse { _state.value = Result.Error(it) }

        return text
    }

    private suspend fun getImage(): String {
        var image = ""

        runCatching {
            imageService.getImage()
        }.mapCatching { response ->
            image = response.first().url
        }.getOrElse { _state.value = Result.Error(it) }

        return image
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}