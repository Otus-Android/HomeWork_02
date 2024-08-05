package otus.homework.coroutines

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    val catsService: CatsService,
    val catsPicturesService: CatsPicturesService,
    val context: Context
) : ViewModel() {

    val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch {
            try {
                coroutineScope {
                    val fact = async<Fact> {
                        catsService.getCatFact()
                    }

                    val picture = async<Picture> {
                        catsPicturesService.getCatPicture()
                    }

                    val catModel = Success(fact.await(), picture.await())
                    _catsLiveData.value = catModel
                }
            } catch (e : SocketTimeoutException) {
                _catsLiveData.value = Error(context.resources.getString(R.string.timeout_error))
            } catch (e : Exception) {
                CrashMonitor.trackWarning(e)
                _catsLiveData.value = Error(e.message)
            }
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val catsPicturesService: CatsPicturesService,
    private val context: Context
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, catsPicturesService, context) as T
}

sealed class Result
data class Success(val fact: Fact, val picture: Picture) : Result()
data class Error(val message: String?) : Result()