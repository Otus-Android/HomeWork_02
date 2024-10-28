package otus.homework.coroutines.viewmodel

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import otus.homework.coroutines.CatsResult
import otus.homework.coroutines.R

class CatsViewImpl(private val view: View) : CatsView {
    private var scope: CoroutineScope? = null

    override fun attachViewModel(viewModel: CatsViewModel) {
        scope = ViewModelCoroutineScope()
        viewModel.cat
            .onEach { populate(it) }
            .launchIn(scope!!)
        callbackFlow<Unit> {
            val button = view.findViewById<Button>(R.id.button)
            button.setOnClickListener { viewModel.loadNewCat() }
            awaitClose { button.setOnClickListener(null) }
        }.launchIn(scope!!)
    }

    override fun detachViewModel() {
        scope?.cancel()
        scope = null
    }

    private fun populate(result: CatsResult) {
        val textView = view.findViewById<TextView>(R.id.fact_textView)
        val imageView = view.findViewById<ImageView>(R.id.cat_imageView)
        when (result) {
            is CatsResult.Init -> {
                textView.text = "Waiting for data"
            }

            is CatsResult.Success -> {
                textView.text = result.fact
                imageView.setImageBitmap(result.image)
            }

            is CatsResult.Error -> {
                textView.text = result.message
            }
        }
    }
}

interface CatsView {
    fun attachViewModel(viewModel: CatsViewModel)
    fun detachViewModel()
}
