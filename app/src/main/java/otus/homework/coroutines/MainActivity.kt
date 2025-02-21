package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Job
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.ViewModelFactory
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatsPresenter

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelFactory())[CatsPresenter::class.java]

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.fact_textView)

        viewModel.catFact.observe(this) { text ->
            textView.text = text ?: getString(R.string.empty_dash)
        }
        viewModel.catImage.observe(this) { catImage ->
            catImage?.into(imageView)
        }
        viewModel.errorHandle.observe(this) { message ->
            message?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
        }

        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                scope.launch {
                    viewModel.updateImage()
                }
                scope.launch {
                    viewModel.updateFact()
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}