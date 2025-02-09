package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private lateinit var catsView: CatsView
    private lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        viewModel = CatsViewModel(diContainer.catFactsService, diContainer.catImagesSourceService)
        catsView.setViewModel(viewModel)
    }

    override fun onResume() {
        super.onResume()
        fetchCatData()
    }

    private fun fetchCatData() {
        viewModel.fetchCatData { result ->
            when (result) {
                is Result.Success -> catsView.populate(result.data)
                is Result.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
