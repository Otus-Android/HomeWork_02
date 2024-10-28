package otus.homework.coroutines.presenter

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.ImageLoader
import otus.homework.coroutines.ImageLoaderImpl
import otus.homework.coroutines.ImageResult
import otus.homework.coroutines.R

class PresenterActivity : AppCompatActivity() {
    private val diContainer = DiContainer()
    private val imageLoader: ImageLoader = ImageLoaderImpl()
    private val catsImageLoader = object : CatsImageLoader {
        override fun load(): Flow<ImageResult> {
            return imageLoader.load(Uri.parse("https://cataas.com/cat"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.cats_view, null)
        setContentView(view)

        lifecycleScope.launch {
            val catsPresenter = CatsPresenterImpl(diContainer.service, catsImageLoader)
            val catsView = CatsViewImpl(view)
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                catsView.onFinishInflate(catsPresenter)
                catsPresenter.attachView(catsView)
                catsPresenter.loadFact()
            }
            lifecycle.repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                catsPresenter.detachView()
            }
        }
    }
}