package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val factTextView: TextView by lazy { findViewById(R.id.fact_textView) }
    private val imageView: ImageView by lazy { findViewById(R.id.image) }
    private val button: Button by lazy { findViewById(R.id.button) }

    fun setOnButtonClickListener(action: () -> Unit) {
        button.setOnClickListener { action() }
    }

     fun populate(catFact: CatFact) {
         factTextView.isInvisible = false
         imageView.isInvisible = false

         factTextView.text = catFact.fact.fact

         Picasso
             .get()
             .load(catFact.image.url)
             .into(imageView)
    }

    fun hide() {
        factTextView.isInvisible = true
        imageView.isInvisible = true

        imageView.setImageDrawable(null)
    }

    fun showSocketTimeoutError() {
        showToast(context.getString(R.string.socket_timeout_error))
    }

    fun showToast(text: String?) {
        Toast.makeText(
            this.context,
            text ?: context.getString(R.string.unknown_error),
            Toast.LENGTH_LONG
        ).show()
    }
}