package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catsPresenter = CatsPresenter(diContainer.service, diContainer.service2)

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var jobPopulateCatsFacts: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        if (exception is java.net.SocketTimeoutException) {
            Toast.makeText(
                this@MainActivity,
                "Не удалось получить ответ от сервером",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(this@MainActivity, exception.localizedMessage, Toast.LENGTH_LONG).show()
            CrashMonitor.trackWarning()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.clickListener = { populateFact(view) }
    }

    private fun populateFact(view: CatsView) {
        jobPopulateCatsFacts = presenterScope.launch(coroutineExceptionHandler) {
            catsPresenter.populateFact()
                .combine(catsPresenter.populateRandomImage()) { fact, image ->
                    Pair(fact.fact, image.url)
                }
                .collect { (fact, imageUrl) -> view.populate(fact, imageUrl) }
        }
    }

    override fun onStop() {
        jobPopulateCatsFacts?.cancel()
        super.onStop()
    }
}