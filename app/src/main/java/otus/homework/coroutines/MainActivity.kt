package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
    //TODO If we don't want to use ViewModel
    private val useViewModel = true

    private lateinit var catsPresenter: ICatsPresenter

    private val diContainer = ApiService.DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        if (useViewModel){
            catsPresenter = ViewModelProvider(
                this,
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CatsViewModel(diContainer){
                            onShowErrorMessage(if (it is SocketTimeoutException) getString(R.string.timeout_exception_message) else it.localizedMessage.orEmpty())
                        } as T
                    }
                }
            )[CatsViewModel::class.java]
        } else {
            catsPresenter = CatsPresenter(diContainer) {
                onShowErrorMessage(if (it is SocketTimeoutException) getString(R.string.timeout_exception_message) else it.localizedMessage.orEmpty())
            }
        }

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
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