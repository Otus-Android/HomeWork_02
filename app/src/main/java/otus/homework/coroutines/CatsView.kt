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
            viewModel?.loadFact()
        }
    }

    override fun populate(result: Result) {
        when (result) {
            is Result.Error -> Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
            is Result.Success -> {
                findViewById<TextView>(R.id.fact_textView).text = result.fact.fact
                Picasso
                    .get()
                    .load(result.fact.url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(findViewById<ImageView>(R.id.cat_image))
            }
        }

    }
}

interface ICatsView {
    fun populate(result: Result)
}