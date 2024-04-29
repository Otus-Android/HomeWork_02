package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //    private lateinit var catsPresenter: CatsPresenter
    //    private val presenterScope = PresenterScope()
    private val diContainer = DiContainer()

    private val viewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(diContainer.serviceCatFact, diContainer.serviceCatImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.serviceCatFact, diContainer.serviceCatImage)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//
//        presenterScope.launch {
//            catsPresenter.onInitComplete()
//        }

        with(viewModel) {
            view.viewModel = this

            catsLiveData.observe(this@MainActivity) { result ->
                when (result) {
                    is Error -> view.showToast(result.message)
                    is Success -> {
                        view.populate(result.fact)
                        view.renderingImage(result.catImage)
                    }
                }
            }

            onInitComplete()
        }
    }

    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//            presenterScope.cancel()
//        }
        super.onStop()
    }
}