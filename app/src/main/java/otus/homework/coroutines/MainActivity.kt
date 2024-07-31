package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service , diContainer.service2) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)
        catsView.presenter = catsViewModel
        catsViewModel.catsModel.observe(this) { result ->
            when (result) {
                is Success -> catsView.populate(result.modelFact)
                Error -> Toast
                    .makeText(this, TEXT_TOAST, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        private const val TEXT_TOAST = "Не удалось получить ответ от сервера"
    }
}