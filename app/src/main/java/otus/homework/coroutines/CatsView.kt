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

    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    override fun populate(result: Result<SomeCatFact>) {
        when (result) {
            is Result.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                findViewById<TextView>(R.id.fact_textView).text = ""
                findViewById<ImageView>(R.id.image).setImageResource(R.drawable.ic_image_error)
            }

            is Result.Success -> {
                findViewById<TextView>(R.id.fact_textView).text = result.data.factText
                Picasso.get()
                    .load(result.data.imageUrl)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(findViewById<ImageView>(R.id.image))
            }
        }
    }
}

interface ICatsView {

    fun populate(result: Result<SomeCatFact>)
}