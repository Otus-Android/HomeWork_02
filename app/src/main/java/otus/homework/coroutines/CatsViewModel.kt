package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) : ViewModel() {

    private val _contentState = MutableStateFlow<Result>(Result.Success(Fact("", 1), ""))
    val contentState = _contentState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->

        when (ex) {
            is SocketTimeoutException -> showError("Не удалось получить ответ от сервера")
            else -> {
                ex.message?.let {
                    showError(it)
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun onInitComplete() {

        viewModelScope.launch(exceptionHandler + Dispatchers.Default) {

            val factDeferred = async {
                with(catsService.getCatFact()) {
                    when {
                        isSuccessful.not() || body() == null -> throw Exception("Wrong response!")
                        else -> body()!!
                    }
                }
            }

            val pictureDeferred = async {


                with(imagesService.getRandomImageUrl()) {
                    when {
                        isSuccessful.not() || body() == null -> throw Exception("Wrong response!")
                        else -> body()!!
                    }
                }
            }

            applyContent(factDeferred.await(), pictureDeferred.await().first().url)
        }

    }

    private fun applyContent(fact: Fact, imageUrl: String) {
        _contentState.value = Result.Success(fact, imageUrl)
    }

    private fun showError(errorText: String) {
        _contentState.value = Result.Error(errorText)
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imagesService) as T
}
