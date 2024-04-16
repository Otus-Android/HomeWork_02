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

    var presenter :CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(data: ModelPresentation) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact.fact
        val im = findViewById<ImageView>(R.id.image)
        Picasso.get()
            .load(data.imageCat.url)
            .into(im)
    }
}

interface ICatsView {

    fun populate(data: ModelPresentation)
}
