package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val catsCoroutineScope: CatsCoroutineScope
): ViewModel() {
    private val _modelLiveData = MutableLiveData<FactPresentationModel>()
    val modelLiveData: LiveData<FactPresentationModel> = _modelLiveData

    private val _onErrorLiveData = MutableLiveData<Int>()
    val onErrorLiveData: LiveData<Int> = _onErrorLiveData

    fun onInitComplete() {
        viewModelScope.launch {
            try {
                val fact = catsCoroutineScope.async { catsService.getCatFact() }
                val image = catsCoroutineScope.async { imageService.getRandomImage() }
                val model = FactPresentationModel(fact.await(), image.await().first())
                _modelLiveData.value = model

            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _onErrorLiveData.value = R.string.connection_error_message
                    }
                    is CancellationException -> {
                        _onErrorLiveData.value = R.string.connection_error_message
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }
}