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

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catsData: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = catsData.fact
        Picasso.get().load(catsData.image.url).into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showError(exceptionType: Int, message: String?) {
        val text = when (exceptionType) {
            SOCKET_TIMEOUT_EXCEPTION -> context.getString(R.string.socket_timeout_exception)
            else -> message ?: context.getString(R.string.unknown_exception)
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val OTHER_EXCEPTION = 0
        const val SOCKET_TIMEOUT_EXCEPTION = 1
    }
}

interface ICatsView {

    fun populate(catsData: CatsData)
    fun showError(exceptionType: Int, message: String? = null)
}