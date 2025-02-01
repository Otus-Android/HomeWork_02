package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineExceptionHandler
import otus.homework.coroutines.dto.Result
import otus.homework.coroutines.vm.CatsPresenterViewModel

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val catsPresenterViewModel: CatsPresenterViewModel by viewModels {
        CatsPresenterViewModel.provideFactory(diContainer.catsFactService, diContainer.catsImagesService, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.catsFactService, diContainer.catsImagesService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        catsPresenterViewModel.onInitComplete()
        view.presenter = catsPresenterViewModel

        catsPresenterViewModel.catImageFact.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    val image = result.data.first
                    val fact = result.data.second
                    view.populate(fact, image)
                }

                is Result.Error ->
                    view.onError(result.message!!)
            }

        })

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}