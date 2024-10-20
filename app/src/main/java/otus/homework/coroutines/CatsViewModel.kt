package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    val state: MutableStateFlow<Result<MainUiModel>> = MutableStateFlow(Result.Noting())

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() = viewModelScope.launch(handler) {
        var text = ""
        var image = ""
        runCatching {
            catsService.getCatFact()
        }.mapCatching { response ->
            if (response.isSuccessful && response.body() != null)
                text = response.body()?.fact.orEmpty()
            else
                CrashMonitor.trackWarning()
        }.getOrElse { Result.Error<MainUiModel>(it) }

        runCatching {
            imageService.getImage()
        }.mapCatching { response ->
            if (response.isSuccessful && response.body() != null)
                image = response.body()?.first()?.url.orEmpty()
            else
                CrashMonitor.trackWarning()
        }.getOrElse { Result.Error<MainUiModel>(it) }
        state.value = Result.Success(MainUiModel(text, image))
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}