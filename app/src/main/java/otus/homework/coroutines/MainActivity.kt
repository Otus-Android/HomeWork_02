package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.Result

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private lateinit var viewModel : CatsViewModel
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

       /* catsPresenter = CatsPresenter(diContainer.service, diContainer.image)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)*/

        viewModel = CatsViewModel(diContainer.service, diContainer.image)
        view.viewModel = viewModel
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.catData.collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.apply {
                            view.populateFact(fact)
                            image?.url?.let {
                                view.populateImage(it)
                            }
                        }
                    }
                    is Result.Error -> Toast.makeText(this@MainActivity, result.errorMessage, Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}