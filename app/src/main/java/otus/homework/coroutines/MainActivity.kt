package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceImage)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        catsViewModel = CatsViewModel(diContainer.service, diContainer.serviceImage)
        view.viewModel = catsViewModel
        catsViewModel.catsLiveData.observe(this) { result ->
            when (result) {
                is Success -> view.populate(result.catsModel)
                is Error -> Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        }
        catsViewModel.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}