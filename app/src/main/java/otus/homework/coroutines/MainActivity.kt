package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var viewModel: CatsViewModel? = null

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = CatsViewModel(
            diContainer.serviceFact,
            diContainer.serviceImage,
        )
        view.viewModel = viewModel
        viewModel?.attachView(view)
        viewModel?.loadFact()
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel?.detachView()
        }
        super.onStop()
    }
}