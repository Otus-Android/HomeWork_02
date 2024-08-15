package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.fact
        val imageView = findViewById<ImageView>(R.id.iv_cat_image)
        Picasso.get().load(cat.image.url).into(imageView)
    }

    override fun showTimeoutError() {
        val textError = ContextCompat.getString(context, R.string.timeout_error)
        Toast.makeText(context, textError, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String?) {
        val textError = message ?: ContextCompat.getString(context, R.string.unknown_error)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cat: Cat)
    fun showTimeoutError()
    fun showError(message: String?)
}