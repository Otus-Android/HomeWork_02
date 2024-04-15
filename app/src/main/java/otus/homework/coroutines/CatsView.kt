package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet? = null,
    defStyleAttr : Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(uiState : CatsUiState) {
        findViewById<TextView>(R.id.fact_textView).text = uiState.fact
        Picasso
            .get()
            .load(uiState.pictureUrl)
            .placeholder(R.drawable.no_image)
            .into(findViewById<AppCompatImageView>(R.id.cat_imageView))
    }

    override fun showToast(message : String) {
        Toast
            .makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }
}

interface ICatsView {

    fun populate(uiState : CatsUiState)
    fun showToast(message : String)
}