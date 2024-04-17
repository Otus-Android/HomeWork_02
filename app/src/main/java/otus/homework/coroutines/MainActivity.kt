package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        viewModel = CatsViewModelFactory(diContainer.service, diContainer.imageService)
            .create(CatsViewModel::class.java)
        view.viewModel = viewModel
        viewModel.attachView(view)
        viewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.detachView()
        }
        super.onStop()
    }
}