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

    var presenter :CatsPresenter? = null
    var viewModel :CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(model: CatsModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact.fact

        val imageView = findViewById<ImageView>(R.id.picture_imageView)
        Picasso.get().load(model.picture.file).into(imageView)
    }

    override fun toast(msg :String?) {
        Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun populate(model: CatsModel)
    fun toast(msg: String?)
}