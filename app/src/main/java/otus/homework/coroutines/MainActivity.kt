package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel = CatsViewModel(diContainer.service, diContainer.serviceImage)
        view.viewModel = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()

        catsViewModel.state.onEach {
            when (it) {
                is Result.Error -> {
                    showErrorToast(it.error)
                }

                is Result.Noting -> {}
                is Result.Success -> view.populate(it.mainUiModel)
            }
        }.launchIn(lifecycleScope)
    }

    private fun showErrorToast(e: Throwable) {
        when (e) {
            is SocketTimeoutException -> {
                Toast.makeText(
                    applicationContext,
                    applicationContext?.getString(R.string.socket_timeout_exception),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                CrashMonitor.trackWarning()
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
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