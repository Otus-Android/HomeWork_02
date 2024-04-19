package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val factory = CatsViewModelFactory(diContainer.service)
        viewModel = ViewModelProvider(this, factory)[CatsViewModel::class.java]
        view.viewModel = viewModel

        viewModel.catsData.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error -> view.showError(result.message)
            }
        })

        viewModel.loadCatData()
    }
}