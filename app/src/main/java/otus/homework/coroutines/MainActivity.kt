package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.cancel

class MainActivity : AppCompatActivity() {

    //    private lateinit var catsPresenter: CatsPresenter
    private lateinit var catsView: CatsView
    private val diContainer = DiContainer()

    private val viewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(diContainer.serviceCatFact, diContainer.serviceCatImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

//        catsPresenter = CatsPresenter(diContainer.serviceCatFact, diContainer.serviceCatImage)
//        catsView.presenter = catsPresenter
//        catsPresenter.attachView(catsView)

        with(viewModel) {
            catsView.viewModel = this

            catsLiveData.observe(this@MainActivity) { result ->
                when (result) {
                    is Error -> catsView.showToast(result.message)
                    is Success -> {
                        catsView.populate(result.fact)
                        catsView.renderingImage(result.catImage)
                    }
                }
            }

            onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsView.presenterScope.cancel()
//            catsPresenter.detachView()
        }
        super.onStop()
    }
}