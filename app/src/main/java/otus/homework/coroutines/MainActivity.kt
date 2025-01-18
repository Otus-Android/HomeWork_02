package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val factory = ViewModelFactory(
            catsService = diContainer.service,
            imageService = diContainer.imageService,
        ) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        catsViewModel = ViewModelProvider(this, factory)[CatsViewModel::class]
        view.bindViewModel(catsViewModel)
    }
}