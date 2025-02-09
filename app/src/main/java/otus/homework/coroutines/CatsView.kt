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
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var viewModel: CatsViewModel

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.fetchCatData { result ->
                when (result) {
                    is Result.Success -> populate(result.data)
                    is Result.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun setViewModel(catsViewModel: CatsViewModel) {
        this.viewModel = catsViewModel
    }

    fun populate(catModel: CatPresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact
        showCatImage(catModel.imageUrl)
    }

    private fun showCatImage(imageUrl: String) {
        val imageView: ImageView = findViewById(R.id.cat_fact_imageView)
        Picasso.get().load(imageUrl).into(imageView)
    }
}
