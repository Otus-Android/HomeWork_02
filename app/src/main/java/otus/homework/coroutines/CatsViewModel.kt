package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    val state: MutableStateFlow<Result<MainUiModel>> = MutableStateFlow(Result.Noting())

    private var _catsView: ICatsView? = null
    private var context: Context? = null

    fun onInitComplete() = viewModelScope.launch {
        context = (_catsView as CatsView).context
        var text = ""
        var image = ""
        runCatching {
            catsService.getCatFact()
        }.mapCatching { response ->
            if (response.isSuccessful && response.body() != null)
                text = response.body()?.fact.orEmpty()
            else
                CrashMonitor.trackWarning()
        }.getOrElse { ::showErrorToast }

        runCatching {
            imageService.getImage()
        }.mapCatching { response ->
            if (response.isSuccessful && response.body() != null)
                image = response.body()?.first()?.url.orEmpty()
            else
                CrashMonitor.trackWarning()
        }.getOrElse { ::showErrorToast }
        state.value = Result.Success(MainUiModel(text, image))
    }

    fun showErrorToast(e: Throwable) {
        when (e) {
            is SocketTimeoutException -> {
                Toast.makeText(
                    context,
                    context?.getString(R.string.socket_timeout_exception),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                CrashMonitor.trackWarning()
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}