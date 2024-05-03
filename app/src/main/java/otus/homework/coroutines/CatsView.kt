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

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact.fact

        Picasso.get()
            .load(catModel.image.first().url)
            .resize(200, 200)
            .centerCrop()
            .into(findViewById<ImageView>(R.id.catImageView))
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun showToast() {
        Toast.makeText(
            context,
            context.getString(R.string.server_error_not_responding),
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)
    fun showToast(text: String)
    fun showToast()
}