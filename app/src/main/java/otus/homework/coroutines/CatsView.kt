package otus.homework.coroutines

import android.content.Context

import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso


class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel?.getData()
        }
    }

    override fun populate(catsData: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = catsData.fact
        Picasso.get().load(catsData.image.url).into(findViewById<ImageView>(R.id.cat_imageView))
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }
}

interface ICatsView {

    fun populate(catsData: CatsData)
    fun showError(message: String)
}


data class Image(
    @field:SerializedName("url")
    val url: String
)

data class CatsData(
    val fact: String,
    val image: Image
)
