package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var viewModel: CatsViewModel? = null
    private val observer = Observer<Result> { state ->
        when (state) {
            is Result.Success -> populate(state.cat)
            is Result.Error -> showToast(state)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel?.liveData?.observeForever(observer)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel?.liveData?.removeObserver(observer)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onButtonClick()
        }
    }

    private fun populate(cat: CatPresentation) {
        findViewById<ImageView>(R.id.imageView).run {
            Picasso
                .get()
                .load(cat.imageUrl)
                .into(this)
        }
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
    }

    private fun showToast(errorType: Result.Error) {
        val message = when (errorType) {
            is Result.Error.SocketTimeout -> {
                context.getString(R.string.socket_timeout_exception_message)
            }

            is Result.Error.Other -> errorType.message
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
