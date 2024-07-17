package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CatsViewModelFactory(
    private val catsFactService: CatsService,
    private val catsPicturesService: CatsPicturesService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatFactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatFactViewModel(catsFactService, catsPicturesService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}