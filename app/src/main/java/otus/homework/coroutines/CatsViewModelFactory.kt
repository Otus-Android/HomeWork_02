package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory(private val service: CatsService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatsViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
