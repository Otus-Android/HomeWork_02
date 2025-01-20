package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModel.CatsViewModelFactory(diContainer.catsService, diContainer.imageService)
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        viewModel.onInitComplete()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is Result.Initialization -> Unit
                        is Result.Success -> view.populate(state.dataModel)
                        is Result.Error -> view.showToast(state.text)
                    }
                }
            }
        }
    }
}
