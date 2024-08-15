package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
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

//    var presenter: CatsPresenter? = null
//
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
//        }
//    }

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_text_view).text = cat.fact.fact
        val imageView = findViewById<ImageView>(R.id.iv_cat_image)
        Picasso.get().load(cat.image.url).into(imageView)
        progressBarVisibility(false)
    }

    override fun showTimeoutError() {
        val textError = ContextCompat.getString(context, R.string.timeout_error)
        Toast.makeText(context, textError, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String?) {
        val textError = message ?: ContextCompat.getString(context, R.string.unknown_error)
        Toast.makeText(context, textError, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        progressBarVisibility(true)
    }

    override fun onMoreClicked(click: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            click()
        }
    }

    private fun progressBarVisibility(visible: Boolean) {
        val progressBarVisibility = if (visible) View.VISIBLE else View.GONE
        val otherViewVisibility = if (visible) View.GONE else View.VISIBLE
        findViewById<ProgressBar>(R.id.progress_bar).visibility = progressBarVisibility
        findViewById<TextView>(R.id.fact_text_view).visibility = otherViewVisibility
        findViewById<ImageView>(R.id.iv_cat_image).visibility = otherViewVisibility
    }
}

interface ICatsView {
    fun populate(cat: Cat)
    fun showTimeoutError()
    fun showError(message: String?)
    fun showProgressBar()
    fun onMoreClicked(click: () -> Unit)

}