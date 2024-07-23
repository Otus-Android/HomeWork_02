package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var clickListener: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            clickListener?.invoke()
        }
    }

    override fun populate(fact: String, imageUrl: String) {
        findViewById<TextView>(R.id.fact_textView).text = fact

        Picasso.get().load(imageUrl).into(findViewById<ImageView>(R.id.image))
    }
}

interface ICatsView {

    fun populate(fact: String, imageUrl: String)
}