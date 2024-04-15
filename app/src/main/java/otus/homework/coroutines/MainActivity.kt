package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter : CatsPresenter
//    private val catsViewModel by viewModels<CatsViewModel> {
//        CatsViewModelFactory(
//            catsService = diContainer.service,
//            pictureService = diContainer.pictureService,
//            catsUiStateMapper = diContainer.catsUiStateMapper,
//        )
//    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catsService = diContainer.service,
            pictureService = diContainer.pictureService,
            catsUiStateMapper = diContainer.catsUiStateMapper,
        )

//        view.viewModel = catsViewModel
//        catsViewModel.attachView(view)
//        catsViewModel.onInitComplete()

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
//            catsViewModel.detachView()
        }
        super.onStop()
    }
}