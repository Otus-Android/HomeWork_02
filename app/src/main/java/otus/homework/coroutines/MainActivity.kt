package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
    //TODO If we don't want to use ViewModel
    private val useViewModel = true

    private val diContainer = ApiService.DiContainer()
    private lateinit var catsPresenter: ICatsPresenter
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        if (useViewModel){
            view.catsRefresh = catsViewModel
            catsViewModel.catsLiveData.observe(this) { result ->
                when (result) {
                    is Success -> view.populate(result)
                    is Error -> onShowErrorMessage(result.message)
                }
            }
            catsViewModel.onInitComplete()
        } else {
            catsPresenter = CatsPresenter(diContainer) {
                onShowErrorMessage(if (it is SocketTimeoutException) getString(R.string.timeout_exception_message) else it.localizedMessage.orEmpty())
            }

            view.catsRefresh = catsPresenter
            catsPresenter.attachView(view)
            catsPresenter.onInitComplete()
        }
    }

    override fun onStop() {
        catsPresenter.cancelJob()

        if (isFinishing) {
            catsPresenter.detachView()
        }

        super.onStop()
    }

    private fun onShowErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}