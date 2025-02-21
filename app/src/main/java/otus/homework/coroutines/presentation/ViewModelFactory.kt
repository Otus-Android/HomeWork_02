package otus.homework.coroutines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.data.CatsRepositoryImpl
import otus.homework.coroutines.domain.CatsInteractor

class ViewModelFactory : ViewModelProvider.Factory {

    private val catsRepository by lazy(LazyThreadSafetyMode.NONE) {
        CatsRepositoryImpl()
    }
    private val catsInteractor by lazy(LazyThreadSafetyMode.NONE) {
        CatsInteractor(catsRepository = catsRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsPresenter(
            catsInteractor = catsInteractor
        ) as T
    }
}