package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        viewModel = CatsViewModelFactory(
            diContainer.service,
            diContainer.imageService,
            diContainer.catsCoroutineScope
        ).create(CatsViewModel::class.java)

        viewModel.onInitComplete()

        observeViewModel()

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }

    private fun observeViewModel() {
        viewModel.modelLiveData.observe(this) {factPresentationModel ->
            populate(factPresentationModel)
        }

        viewModel.onErrorLiveData.observe(this) {
            onError(it)
        }
    }

    private fun onError(textResId: Int) {
        Toast.makeText(this, getText(textResId), Toast.LENGTH_LONG).show()
    }

    private fun populate(factPresentationModel: FactPresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text = factPresentationModel.fact.fact
        Picasso.get().load(factPresentationModel.imageEntity.url).into(findViewById<ImageView>(R.id.fact_imageView))
    }
}