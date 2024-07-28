package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException

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

    override fun populate(result: Result) {
        when (result) {
            is Result.Success<*> -> {
                findViewById<TextView>(R.id.fact_textView).text = result.catInfo.fact.fact
                Picasso.with(context)
                    .load(result.catInfo.url)
                    .into(findViewById<ImageView>(R.id.fact_textView))
            }

            is Result.Error -> {
                when (result.error) {
                    is SocketTimeoutException -> {
                        showToast("Не удалось получить ответ от сервером")
                    }

                    else -> {
                        showToast(result.error.message.toString())
                    }
                }
            }
        }
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(result: Result)

    fun showToast(text: String)
}