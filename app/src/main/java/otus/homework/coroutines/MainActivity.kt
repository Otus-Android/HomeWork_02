package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var catsViewModel: CatViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel =
            ViewModelProvider(this, ViewModelFactory(diContainer)).get(CatViewModel::class.java)

        /*catsPresenter = CatViewModel(diContainer.catsFactService, diContainer.catsImageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/

        view.presenter = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()

        catsViewModel.liveData.observe(this) {
            when (it) {
                is Result.Success -> view.populate(it.catModel)
                is Result.Error -> view.toast(it.message)
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            //catsPresenter.detachView()
            catsViewModel.detachView()
        }
        super.onStop()
    }

    class ViewModelFactory(private val diContainer: DiContainer) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
                return CatViewModel(diContainer.catsFactService, diContainer.catsImageService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
