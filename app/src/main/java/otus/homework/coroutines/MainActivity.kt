package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private lateinit var catsPresenter: CatsPresenter
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.serviceCatFact,
            diContainer.serviceCatImage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            diContainer.serviceCatFact,
            diContainer.serviceCatImage
        )

        // TODO non-optimal solution with one place to toggle between P and VM, but it's easier to check

        // Presenter
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        // ViewModel
//        view.viewModel = catsViewModel
//        catsViewModel.catsLiveData.observe(this) { result ->
//            when (result) {
//                is Success -> view.populate(result.cat)
//                is Error -> view.showToast(result.message)
//            }
//        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}