package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private val scope = PresenterScope()

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            scope.launch {
                Log.d("onFinishInflate", "Presenter = '$presenter'")
                presenter?.onInitComplete(context)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope.cancelJobs()
    }

    override fun populate(cat: Cat) {
        Log.d("populate", "Got cat = '$cat'")
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.fact
        val imageView: ImageView = findViewById(R.id.picture_imageView)
        Picasso.get().load(cat.picture.url).into(imageView)
    }
}

interface ICatsView {

    fun populate(cat: Cat)
}
