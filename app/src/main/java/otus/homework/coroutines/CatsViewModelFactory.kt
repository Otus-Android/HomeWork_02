package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val pictureService: PicturesService,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, pictureService) as T
    }
}