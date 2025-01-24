package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private val viewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        catsPresenter = CatsPresenter()
        setContentView(view)
        viewModel.getCat()
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is Result.Success -> catsPresenter.showCatFactAndPicture(it.cat)
                    is Result.Error -> catsPresenter.showOrLogError(it.exception, this@MainActivity)
                    is Result.Loading -> catsPresenter.showLoading()
                }
            }
        }
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.setOnClickListener {
            Log.d("MainActivity.setOnClickListener", "onClick set")
            viewModel.getCat()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}
