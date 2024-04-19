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

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }
    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun populate(fact: CatFactAndImage) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact.fact
        Picasso.get()
            .load(fact.imageUrl)
            .resize(100,100)
            .centerCrop()
            .into(findViewById<ImageView>(R.id.image))
    }
}

interface ICatsView {

    fun showError(message:String)
    fun populate(fact: CatFactAndImage)
}