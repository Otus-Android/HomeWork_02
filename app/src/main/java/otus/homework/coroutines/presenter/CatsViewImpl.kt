package otus.homework.coroutines.presenter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import otus.homework.coroutines.CatsResult
import otus.homework.coroutines.R

class CatsViewImpl(private val view: View) : CatsView {
    fun onFinishInflate(presenter: CatsPresenter) {
        view.findViewById<Button>(R.id.button).setOnClickListener {
            presenter.loadFact()
        }
    }

    override fun populate(result: CatsResult) {
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
    fun populate(result: CatsResult)
}
