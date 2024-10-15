package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun populate(content: CatsContent) {
        findViewById<TextView>(R.id.fact_textView).text = content.fact.fact
        Picasso.get().load(content.image.url).into(findViewById<ImageView>(R.id.cats_imageView))
    }
}

interface ICatsView {
    fun showToast(message: String)
    fun populate(content: CatsContent)
}