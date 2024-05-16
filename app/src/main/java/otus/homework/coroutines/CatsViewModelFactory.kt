package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val catsCoroutineScope: CatsCoroutineScope
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, imageService, catsCoroutineScope) as T
    }
}