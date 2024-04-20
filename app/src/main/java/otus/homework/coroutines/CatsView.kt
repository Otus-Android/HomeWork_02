package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :ICatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    private fun updateData(catData: CatData){
        findViewById<TextView>(R.id.fact_textView).text = catData.fact
        Picasso.get()
            .load(catData.imageUrl)
            .resize(100,100)
            .centerCrop()
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_not_found)
            .into(findViewById<ImageView>(R.id.imageCat))
    }

    override fun populate(catData: CatData) {
        updateData(catData)
    }

    override fun populate(result: Result<CatData>) {
        when (result) {
            is Result.Error -> {
                updateData(emptyCatData)
            }

            is Result.Success -> {
                updateData(result.data)
            }
        }
    }
}

interface ICatsView {
    fun populate(catData: CatData)
    fun populate(result: Result<CatData>)
}