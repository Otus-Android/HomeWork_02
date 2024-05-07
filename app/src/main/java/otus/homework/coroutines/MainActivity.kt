package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModel.CatsViewModelFactory(diContainer.serviceFact, diContainer.serviceImage)
    }

    private val diContainer = DiContainer()
    private val presenterScope = PresenterScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()

        catsViewModel.getData().observe(this) { result ->
            when (result) {
                is Success -> view.populate(result.catModel)
                is TimeoutException -> view.showToast()
                is Error -> {
                    view.showToast(result.message.orEmpty())
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsViewModel.detachView()
        }
        super.onStop()
    }
}