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

    override fun populate(cat: CatPresentation) {
        findViewById<ImageView>(R.id.imageView).run {
            Picasso
                .get()
                .load(cat.imageUrl)
                .into(this)
        }
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
    }

    override fun showToast(exceptionType: ExceptionType) {
        val message = when (exceptionType) {
            is ExceptionType.SocketTimeout -> {
                context.getString(R.string.socket_timeout_exception_message)
            }

            is ExceptionType.Other -> exceptionType.message
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(cat: CatPresentation)

    fun showToast(exceptionType: ExceptionType)
}
