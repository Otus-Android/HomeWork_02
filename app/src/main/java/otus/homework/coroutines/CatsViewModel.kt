package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val apiService: ApiService
) : ViewModel(), ICatsRefresh {

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    override fun onInitComplete() {
        viewModelScope.launch {
            try {
                val factResult =
                    async { apiService.serviceCatFact.getCatFact().fact }
                val imageResult =
                    async { apiService.serviceCatImage.getCatImage().firstOrNull()?.url.orEmpty() }

                val catData =
                    CatData(
                        fact = factResult.await(),
                        imageUrl = imageResult.await()
                    )
                _catsLiveData.value = Success(catData)
            }
            catch (e : Exception){
                showError(e)
            }
        }
    }

    private fun showError(throwable: Throwable) {
        _catsLiveData.value = Error(throwable.localizedMessage.orEmpty())

        if (throwable is SocketTimeoutException) return

        CrashMonitor.trackWarning(throwable.localizedMessage.orEmpty())
    }
}