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

    var presenter : CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(modelFact: ModelFact) {
        findViewById<TextView>(R.id.fact_textView).text = modelFact.fact?.fact
        val imageView = findViewById<ImageView>(R.id.image)
        Picasso
            .get()
            .load(modelFact.imageCat?.url)
            .into(imageView)
    }
}

interface ICatsView {

    fun populate(modelFact: ModelFact)
}