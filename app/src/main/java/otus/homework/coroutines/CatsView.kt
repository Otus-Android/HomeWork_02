package otus.homework.coroutines

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf


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
        findViewById<Button>(R.id.button2).setOnClickListener {
            // для проверки отмены корутин
            val switchActivityIntent = Intent(context, MainActivity2::class.java)
            startActivity(context, switchActivityIntent, bundleOf())
        }
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun handle(@StringRes resId: Int) {
        handle(context.getString(resId))
    }

    override fun handle(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView: IUserMessageHandler {

    fun populate(fact: Fact)
}