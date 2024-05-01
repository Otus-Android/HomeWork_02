package otus.homework.coroutines

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var catsPresenter: CatsPresenter
    private lateinit var viewModel: CatsViewModel
    private lateinit var catsObserver: Observer<Result<CatInfo>>

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<TextView>(R.id.button).setOnClickListener {
            viewModel.getCatInfo()
        }

        val viewModelFactory = CatsViewModelFactory(diContainer)

        viewModel = ViewModelProvider(this, viewModelFactory)[CatsViewModel::class.java]

        catsObserver = Observer { result ->
            when (result) {
                is Result.Success -> {
                    findViewById<TextView>(R.id.fact_textView).text = result.data.fact
                    Picasso.get().load(result.data.imageUrl).into(findViewById<ImageView>(R.id.imageView))
                }
                is Result.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.catsInfo.observe(this, catsObserver)

        viewModel.getCatInfo()

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.catImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.catsInfo.removeObserver(catsObserver)
    }
}