package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
}

interface ICatsView {
    fun showToast(message: String)
    fun populate(fact: Fact)
}