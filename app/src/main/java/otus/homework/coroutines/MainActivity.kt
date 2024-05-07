package otus.homework.coroutines

import android.content.Context
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private val diContainer = DiContainer()

    /* MVVM */
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(
            diContainer.catFactService, diContainer.catImageService, resources
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.updateFact()
        }

        viewModel.uiState.observe(this) {
            when (it) {
                is MainViewModel.Result.Success -> {
                    val state = it.state
                    findViewById<TextView>(R.id.fact_textView).text = state.fact.fact

                    val imageView = findViewById<ImageView>(R.id.imageView)

                    val imageUrl = state.image?.url
                    if (!imageUrl.isNullOrEmpty()) {
                        Picasso.get().load(imageUrl).into(imageView)
                    } else {
                        imageView.setImageDrawable(null)
                    }
                }

                is MainViewModel.Result.Error -> {
                    Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                }

                else -> {
                }
            }
        }

        viewModel.updateFact()
    }

    /* MVP */
    /*lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter =
            CatsPresenter(diContainer.catFactService, diContainer.catImageService, resources)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }*/
}