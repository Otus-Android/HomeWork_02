package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels


class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private var catsPresenter: CatsPresenter? = null
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.service,
            diContainer.picturesService,
            applicationContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        if ( true ) {
            Log.i("${this::class.java.name}", "viewModel")
            view.viewModel = catsViewModel;
            catsViewModel.catsLiveData.observe(this) { result ->
                when (result) {
                    is Success -> view.populate(CatsModel(result.fact, result.picture))
                    is Error -> view.toast(result.message)
                }
            }
        } else {
            Log.i("${this::class.java.name}", "presenter")
            catsPresenter = CatsPresenter(diContainer.service,
                diContainer.picturesService,
                applicationContext)
            view.presenter = catsPresenter
            catsPresenter?.attachView(view)
            catsPresenter?.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter?.detachView()
        }
        super.onStop()
    }
}