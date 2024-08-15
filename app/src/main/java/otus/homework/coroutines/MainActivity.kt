package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels<CatsViewModel> {
        CatsViewModel.Factory(
            catsFactService = diContainer.catFactService,
            catsImageService = diContainer.catImageService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.onMoreClicked { viewModel.onMoreClicked() }
        lifecycleScope.launch {
            viewModel.uiState.collect { result ->
                when (result) {
                    is CatResult.Success -> view.populate(result.cat)
                    is CatResult.Error -> view.showError(result.message)
                    is CatResult.TimeoutError -> view.showTimeoutError()
                    is CatResult.Loading -> view.showProgressBar()
                    else -> {}
                }
            }
        }
    }

}