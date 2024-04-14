package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(cat: Cat) {
        setFact(cat.fact)
        setPicture(cat.presentation)
    }

    private fun setFact(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    private fun setPicture(presentation: Presentation) {
        Picasso.get()
            .load(presentation.url)
            .into(findViewById<ImageView>(R.id.picture_imageView))
    }
}

interface ICatsView {

    fun populate(cat: Cat)
}