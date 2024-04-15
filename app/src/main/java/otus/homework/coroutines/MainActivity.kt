package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(diContainer.service)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.setOnMoreFactsClickListener { viewModel.loadFact() }

        viewModel.state.observe(this) { state ->
            when (state) {
                is Result.Success<*> -> {
                    (state.value as? CatsFactModel)?.let {
                        view.populate(it)
                    }
                }

                is Result.Error -> {
                    view.showError()
                }
            }
        }
    }


    class CatsViewModelFactory(
        private val catsService: CatsService
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService) as T
        }
    }
}