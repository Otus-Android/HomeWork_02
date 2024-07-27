package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catsPresenter = CatsPresenter(diContainer.service, diContainer.service2)

    private val viewModel = CatsViewModel(diContainer.service, diContainer.service2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)

        view.viewModel = viewModel

        viewModel.resultLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error -> view.showError(result.error.message)
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