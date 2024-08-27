package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private var catsPresenter: CatsPresenter? = null
    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(diContainer.service)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view.setPresenter(catsPresenter)
        catsPresenter?.attachView(view)
        catsPresenter?.onInitComplete()

//        view.setViewModel(catsViewModel)

//        catsViewModel.catsLiveData.observe(this){ result ->
//            when(result){
//                is Result.Error -> view.showError(result.message)
//                is Result.ErrorRes -> view.showError(result.message)
//                is Result.Success<*> -> view.populate(result.catModel as CatModel)
//            }
//        }
//
//        catsViewModel.onInitComplete()

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter?.detachView()
        }
        super.onStop()
    }
}