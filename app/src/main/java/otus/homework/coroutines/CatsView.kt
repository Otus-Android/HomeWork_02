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

    var viewModel: CatsViewModel? = null
    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.apply {
                getCatData()
            }
            presenter?.apply {
                getCatData()
            }
        }
    }

    override fun populateFact(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun populateImage(url: String) {
        Picasso.get().load(url).into(findViewById<ImageView>(R.id.fact_ImageView))
    }
}

interface ICatsView {

    fun populateFact(fact: Fact)
    fun populateImage(url: String)
}