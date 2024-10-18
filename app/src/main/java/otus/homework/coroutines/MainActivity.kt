package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.factService, diContainer.imageService)
        initObservers()
        catsPresenter.initCatStateListener()

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.cancelCatsJob()
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            launch {
                catsPresenter.eventShowErrorConnectToServer.collect { showErrorToast() }
            }
            launch {
                catsPresenter.eventShowExceptionMessage.collect(::showErrorToast)
            }
        }
    }

    private fun showErrorToast(message: String? = null) = Toast
        .makeText(this, message ?: getString(R.string.error_to_connect_message), Toast.LENGTH_SHORT)
        .show()
}