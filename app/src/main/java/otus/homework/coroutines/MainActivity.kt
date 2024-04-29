package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.cancel

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private lateinit var catsView: CatsView

//    private val viewModel by viewModels<CatsViewModel> {
//        CatsViewModelFactory(diContainer.serviceCatFact, diContainer.serviceCatImage)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        catsPresenter = CatsPresenter(diContainer.serviceCatFact, diContainer.serviceCatImage)
        catsView.presenter = catsPresenter
        catsPresenter.attachView(catsView)

//        with(viewModel) {
//            view.viewModel = this
//
//            catsLiveData.observe(this@MainActivity) { result ->
//                when (result) {
//                    is Error -> view.showToast(result.message)
//                    is Success -> {
//                        view.populate(result.fact)
//                        view.renderingImage(result.catImage)
//                    }
//                }
//            }
//
//            onInitComplete()
//        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsView.presenterScope.cancel()
            catsPresenter.detachView()
        }
        super.onStop()
    }
}