package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel = CatsViewModel(
            catsService = diContainer.serviceCats,
            imagesService = diContainer.serviceImages
        )
        view.viewModel = catsViewModel
        catsViewModel.onInitComplete()
    }
}
