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
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatViewModel? = null
    var viewModel: CatViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact
        Picasso.get()
            .load(catModel.photoUrl)
            .into(findViewById<ImageView>(R.id.imageView))
    }

    override fun toast(message: String?) {
        Toast.makeText(context, message ?: "unknown error", Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)
    fun toast(message: String?)
}
