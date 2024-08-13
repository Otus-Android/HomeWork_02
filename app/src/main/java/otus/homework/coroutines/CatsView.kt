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

    //var presenter :CatsPresenter? = null
    var viewModel :CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(catsModel: CatsModel) {
        findViewById<TextView>(R.id.fact_textView).text = catsModel.catsFact.fact
        if (catsModel.catsImage.isNotEmpty()) {
            Picasso.get().load(catsModel.catsImage)
                .into(findViewById<ImageView>(R.id.fact_imageView))
        }
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catsModel: CatsModel)
    fun showToast(message: String?)
}