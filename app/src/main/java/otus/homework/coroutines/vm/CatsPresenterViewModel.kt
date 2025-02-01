package otus.homework.coroutines.vm

import android.os.Bundle
import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatLoader
import otus.homework.coroutines.dto.CatImage
import otus.homework.coroutines.CatsFactService
import otus.homework.coroutines.CatsImageService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.dto.Fact
import otus.homework.coroutines.dto.Result
import kotlin.Pair

class CatsPresenterViewModel(
    private val catsFactService: CatsFactService,
    private val catsImagesService: CatsImageService
) : ViewModel(), CatLoader {

    companion object {
        fun provideFactory(catsFactService: CatsFactService,
                           catsImagesService: CatsImageService,
                           owner: SavedStateRegistryOwner,
                           defaultArgs: Bundle? = null,): AbstractSavedStateViewModelFactory =object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return CatsPresenterViewModel(catsFactService,catsImagesService) as T
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable.message.toString())
        _catImageFact.postValue(Result.Error("", throwable))
    }


    private val _catImageFact = MutableLiveData<Result<Pair<CatImage, Fact>>>()
    val catImageFact: LiveData<Result<Pair<CatImage, Fact>>> get() = _catImageFact

    override fun onInitComplete() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val imageResponse = async{ catsImagesService.getCatImage()}
                val factResponse = async{ catsFactService.getCatFact()}

                val awaitAll = awaitAll(imageResponse, factResponse)

                val image = awaitAll[0] as List<CatImage>
                val fact = awaitAll[1] as Fact

                _catImageFact.postValue(Result.Success(Pair(image[0], fact)))

            } catch (e: Exception) {
                e.printStackTrace()
                _catImageFact.postValue(Result.Error("", e))
            }
        }
    }
}