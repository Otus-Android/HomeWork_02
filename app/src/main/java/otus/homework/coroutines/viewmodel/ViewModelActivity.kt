package otus.homework.coroutines.viewmodel

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.ImageLoader
import otus.homework.coroutines.ImageLoaderImpl
import otus.homework.coroutines.ImageResult
import otus.homework.coroutines.R

class ViewModelActivity : AppCompatActivity() {
    private val diContainer = DiContainer()
    private val imageLoader: ImageLoader = ImageLoaderImpl()
    private val catsImageLoader = object: CatsImageLoader {
        override fun load(): Flow<ImageResult> {
            return imageLoader.load(Uri.parse("https://cataas.com/cat"))
        }
    }
    private val viewModel: CatsViewModel by viewModels<CatsViewModelImpl> {
        CatsViewModelFactory(diContainer.service, catsImageLoader)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.cats_view, null)
        setContentView(view)

        lifecycleScope.launch {
            val catsView = CatsViewImpl(view)
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                catsView.attachViewModel(viewModel)
                viewModel.loadNewCat()
            }
            lifecycle.repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                catsView.detachViewModel()
            }
        }
    }
}