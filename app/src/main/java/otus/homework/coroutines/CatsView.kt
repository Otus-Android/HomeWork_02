package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catModel: CatPresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact
        showCatImage(catModel.imageUrl)
    }

    private fun showCatImage(imageUrl: String) {
        val imageView: ImageView = findViewById(R.id.cat_fact_imageView)
        Picasso.get().load(imageUrl).into(imageView)
    }



}

interface ICatsView {
    fun populate(catModel: CatPresentationModel)
}