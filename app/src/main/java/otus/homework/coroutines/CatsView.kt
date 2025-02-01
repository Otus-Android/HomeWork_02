package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.dto.CatImage
import otus.homework.coroutines.dto.Fact
import otus.homework.coroutines.vm.CatsPresenterViewModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatLoader? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact, image: CatImage) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        Picasso.get().load(image.url).into(findViewById<ImageView>(R.id.cat_imageView));
    }

    override fun onError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {

    fun populate(fact: Fact, image: CatImage)

    fun onError(text: String)

}