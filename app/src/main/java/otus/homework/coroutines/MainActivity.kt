package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()

    private val presenterScope = PresenterScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.serviceCatFact, diContainer.serviceCatImage)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)

        presenterScope.launch {
            catsPresenter.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            presenterScope.cancel()
        }
        super.onStop()
    }
}