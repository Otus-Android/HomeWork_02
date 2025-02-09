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

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun showCatImage(imageUrl: String) {
        val imageView: ImageView = findViewById(R.id.cat_fact_imageView)
        Log.i("debug", "url = $imageUrl")
        // Загружаем изображение с помощью Picasso
        Picasso.get()
            .load(imageUrl)
            .into(imageView)
    }


}

interface ICatsView {
    fun populate(fact: Fact)
    fun showCatImage(imageUrl: String)
}