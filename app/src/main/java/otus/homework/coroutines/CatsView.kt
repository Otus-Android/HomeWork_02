package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.Toast
import com.squareup.picasso.Picasso

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
    }

    override fun populate(catsViewData: CatsViewData) {
        findViewById<TextView>(R.id.fact_textView).text = catsViewData.fact
        Picasso.get().load(catsViewData.imageUrl).into( findViewById<ImageView>(R.id.image_сat) )
    }

    override fun showError(messageError: String) {
        Toast.makeText( context, messageError, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(catsViewData: CatsViewData)

    fun showError(messageError: String)
}