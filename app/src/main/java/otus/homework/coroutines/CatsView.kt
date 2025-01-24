package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var onButtonClick: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            Log.d("onFinishInflate", "onClick = $onButtonClick")
            onButtonClick?.invoke()
        }
    }

    override fun populate(cat: Cat) {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = GONE
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.text
        val imageView: ImageView = findViewById(R.id.picture_imageView)
        Picasso.get().load(cat.picture.url).into(imageView)
    }

    override fun showLoading() {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = VISIBLE
    }

    override fun setButtonOnClickListener(onClick: () -> Unit) {
        Log.d("CatsView.setButtonOnClickListener", "onClick = '$onClick'")
        onButtonClick = onClick
    }
}

interface ICatsView {

    fun populate(cat: Cat)
    fun showLoading()
    fun setButtonOnClickListener(onClick: () -> Unit)
}
