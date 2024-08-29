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
            //presenter?.onInitComplete()
            viewModel?.init()
        }
    }

    override fun populate(cat: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.fact
        val imageV = findViewById<ImageView>(R.id.cat_imageView)
        Picasso.get().load(cat.image.url).into(imageV)
    }

    override fun showToast(error: String?) {
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cat: CatData)

    fun showToast(error: String?)
}