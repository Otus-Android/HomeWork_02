package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.model.CatsInfo

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(data: CatsInfo) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact
        Picasso
            .get()
            .load(data.picture)
            .into(findViewById<AppCompatImageView>(R.id.iv_fact))
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun setOnButtonClickListener(onClickListener: OnClickListener) {
        findViewById<Button>(R.id.button).setOnClickListener(onClickListener)
    }

    fun handle(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun populate(data: CatsInfo)
    fun showError(error: String)
    fun setOnButtonClickListener(onClickListener: View.OnClickListener)
}