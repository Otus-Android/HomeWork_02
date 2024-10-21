package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(diContainer.catFactsService, diContainer.imagesService) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        lifecycleScope.launch {
            viewModel.catFactResultFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collect {
                    when (it) {
                        is CatFactResult.Error -> view.showToast(it.error)
                        CatFactResult.NoFact -> view.hide()
                        CatFactResult.SocketTimeoutError -> view.showSocketTimeoutError()
                        is CatFactResult.Success -> view.populate(it.catFact)
                    }
                }
        }

        view.setOnButtonClickListener {
            viewModel.getCatFact()
        }

        viewModel.getCatFact()
    }

}