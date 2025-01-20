package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private val scope = PresenterScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        scope.launch {
            catsPresenter.onInitComplete(this@MainActivity)
        }
    }

    override fun onStop() {
        scope.cancelJobs()
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}