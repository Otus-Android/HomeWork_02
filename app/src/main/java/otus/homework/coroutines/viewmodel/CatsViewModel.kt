package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsResult
import otus.homework.coroutines.CatsService
import otus.homework.coroutines.ImageResult

interface CatsImageLoader {
    fun load(): Flow<ImageResult>
}

interface CatsViewModel {
    val cat: StateFlow<CatsResult>
    fun loadNewCat()
}

class CatsViewModelImpl(
    private val catsService: CatsService,
    private val imageLoader: CatsImageLoader,
): ViewModel(ViewModelCoroutineScope()), CatsViewModel {
    private val _cat = MutableStateFlow<CatsResult>(CatsResult.Init, )
    override val cat: StateFlow<CatsResult>
        get() = _cat

    private var _currentJob: Job? = null
    override fun loadNewCat() {
        _currentJob?.cancel()
        _currentJob = viewModelScope.launch {
            val factJob = async { catsService.getCatFact() }
            val imageJob = async { imageLoader.load().single() }
            val factResponse = factJob.await()
            val imageResult = imageJob.await()
            if (!factResponse.isSuccessful) {
                _cat.emit(CatsResult.Error("Failed to fetch cat fact"))
            } else if (imageResult is ImageResult.Error) {
                _cat.emit(CatsResult.Error(imageResult.error))
            } else {
                imageResult as ImageResult.Success
                _cat.emit(
                    CatsResult.Success(
                        factResponse.body()!!.fact,
                        imageResult.image
                    )
                )
            }
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageLoader: CatsImageLoader,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModelImpl::class.java)) {
            return CatsViewModelImpl(catsService, imageLoader) as T
        }
        return super.create(modelClass)
    }
}