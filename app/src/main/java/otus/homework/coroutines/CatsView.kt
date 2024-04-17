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

    var viewModel : CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    override fun populate(factPresentationModel: FactPresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text = factPresentationModel.fact.fact
        Picasso.get().load(factPresentationModel.imageEntity.url).into(findViewById<ImageView>(R.id.fact_imageView))
    }

    override fun onError(textResId: Int) {
        Toast.makeText(context, context.getText(textResId), Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun populate(factPresentationModel: FactPresentationModel)
    fun onError(textResId: Int)
}