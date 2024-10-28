package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            catsService = diContainer.factService,
            pictureService = diContainer.pictureService,
        )
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        // TODO presenter
        catsPresenter = CatsPresenter(diContainer.factService, diContainer.pictureService)
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        view.setOnButtonClickListener { catsPresenter.onInitComplete() }

        // TODO viewModel
//        catsViewModel.viewState.observe(this) { result ->
//            when (result) {
//                is Result.Success<*> -> view.populate(result.data as CatsInfo)
//                is Result.Error -> view.showErrorToast(result.error)
//            }
//        }
//        view.setOnButtonClickListener { catsViewModel.getCatInfo() }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}