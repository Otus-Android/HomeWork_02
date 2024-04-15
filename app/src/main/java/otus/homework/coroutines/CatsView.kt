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

    private var onButtonClick: () -> Unit = {}

    fun setOnMoreFactsClickListener(onClick: () -> Unit) {
        onButtonClick = onClick
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onButtonClick()
        }
    }

    override fun populate(model: CatsFactModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        Picasso.get().load(model.image).into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showError() {
        Toast.makeText(context, resources.getText(R.string.server_error), Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(model: CatsFactModel)

    fun showError()
}