package otus.homework.coroutines

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(diContainer.service, diContainer.serviceImages)
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.findViewById<TextView>(R.id.fact_textView).movementMethod = ScrollingMovementMethod()

        view.viewModel = catsViewModel

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceImages)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
               view.listenViewModel()
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