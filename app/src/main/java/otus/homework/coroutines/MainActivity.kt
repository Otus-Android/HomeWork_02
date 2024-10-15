package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.squareup.picasso.Picasso
import otus.homework.coroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModel.CatsViewModelFactory(
            diContainer.factsService,
            diContainer.imageService
        )
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        catsViewModel.init()
        binding.button.setOnClickListener{ catsViewModel.init() }
        catsViewModel.uiStateLiveData.observe(this){ uiState ->
            when(uiState){
                is CatsUiState.Loading -> { showProgressBar() }
                is CatsUiState.Success -> { showContent(uiState.content) }
                is CatsUiState.Error -> { showToastMessage(uiState.errorMessage) }
            }
        }
    }

    private fun showProgressBar(){
        binding.catsProgressBar.visibility = View.VISIBLE
    }

    private fun showContent(catsContent: CatsContent){
        binding.catsProgressBar.visibility = View.GONE
        binding.factTextView.text = catsContent.fact.fact
        Picasso.get().load(catsContent.image.url).into(binding.catsImageView)
    }

    private fun showToastMessage(message: String){
        binding.catsProgressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}