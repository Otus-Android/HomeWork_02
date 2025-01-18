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

    var presenter: CatsPresenter? = null
    private var textView: TextView? = null
    private var imageView: ImageView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById(R.id.fact_textView)
        imageView = findViewById(R.id.image)
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(model: Model) {
        imageView?.let {
            Picasso.get()
                .load(model.imageUrl)
                .into(it)
        }
        textView?.let {
            it.text = model.fact.fact
        }
    }
}

interface ICatsView {

    fun populate(model: Model)
}

data class Model(
    val fact: Fact,
    val imageUrl: String
)