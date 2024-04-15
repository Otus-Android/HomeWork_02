package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            PresenterScope().launch {
                presenter?.onInitComplete()
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun renderingImage(catImages: List<CatImage>) {
        val ivCat = findViewById<ImageView>(R.id.fact_imageView)
        Picasso.get()
            .load(catImages[0].url)
            .into(ivCat)
    }
}

interface ICatsView {
    fun showToast(message: String)
    fun populate(fact: Fact)
    fun renderingImage(catImages: List<CatImage>)
}