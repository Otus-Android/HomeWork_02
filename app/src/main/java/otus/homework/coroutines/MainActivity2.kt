package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MainActivity2 : AppCompatActivity() {


    private val diContainer = DiContainer()
    val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatsViewModel(diContainer.service) as T
                }
            }
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.populate()
        }

        viewModel.liveData.observe(this) { result ->
            when (result){
                is ApiResult.Error ->
                    Toast.makeText(this, result.error.message, Toast.LENGTH_LONG).show()
                is ApiResult.Success -> {
                    Picasso.get().load(result.data.catapiImage[0].url)
                        .into(findViewById<ImageView>(R.id.fact_imgView))
                    findViewById<TextView>(R.id.fact_textView).text = result.data.fact.fact
                }
            }
        }

    }

}