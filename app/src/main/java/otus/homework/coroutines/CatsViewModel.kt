package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsViewModel(
    private val catsService: CatsService,
    private val catImagesService: CatImagesService
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable as Exception)
    }

    fun fetchCatData(onResult: (Result<CatPresentationModel>) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val fact = loadCatFact()
                val imageUrl = loadCatImage()

                if (fact != null && imageUrl != null) {
                    val catModel = CatPresentationModel(fact.fact, imageUrl)
                    onResult(Result.Success(catModel))
                } else {
                    onResult(Result.Error("Факт или изображение не найдены"))
                }
            } catch (e: Exception) {
                onResult(Result.Error(e.message ?: "Произошла ошибка"))
            }
        }
    }

    private suspend fun loadCatFact(): Fact? {
        return withContext(Dispatchers.IO) {
            catsService.getCatFact()
        }
    }

    private suspend fun loadCatImage(): String? {
        return withContext(Dispatchers.IO) {
            val catImageList = catImagesService.getCatImage()
            catImageList.firstOrNull()?.url
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
