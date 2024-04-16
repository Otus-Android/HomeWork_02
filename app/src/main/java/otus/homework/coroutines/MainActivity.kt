package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.service,
            diContainer.service2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.presenter = catsViewModel

        catsViewModel.catsModel.observe(this) { result ->
            when (result) {
                is Success -> view.populate(result.data)
                Error -> Toast.makeText(
                    this,
                    "Не удалось получить ответ от сервером",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
