package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(cat: Cat) {
        val textView = findViewById<TextView>(R.id.fact_textView)
        textView.text = cat.fact
        val imageView = findViewById<AppCompatImageView>(R.id.random_imageView)
        Picasso.get().load(cat.imageUrl).into(imageView)
    }

    fun setOnButtonClickListener(block: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            block()
        }
    }
}

interface ICatsView {

    fun populate(cat: Cat)
}