package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModel.Factory(
            catsRepository = diContainer.repository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.repository)
        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        catsViewModel.uiState.observe(this) { result: Result<Cat> ->
            when (result) {
                is Success -> view.populate(result.data)
                is Error -> {
                    Toast.makeText(
                        this,
                        result.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
        catsViewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}