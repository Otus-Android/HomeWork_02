package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

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

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun showSocketTimeoutError() {
        showToast(context.getString(R.string.socket_timeout_error))
    }

    override fun showToast(text: String?) {
        Toast.makeText(
            this.context,
            text ?: context.getString(R.string.unknown_error),
            Toast.LENGTH_LONG
        ).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact)

    fun showSocketTimeoutError()

    fun showToast(text: String?)
}