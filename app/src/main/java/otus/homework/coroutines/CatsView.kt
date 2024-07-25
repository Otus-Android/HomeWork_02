package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

/**
 * Считаю, что методы во вью не нужно делать suspend.
 * Если ответ от сервера получен, то нет смысла создавать точку приостановки в данных функциях,
 * это лишняя генерация кода под капотом в suspend функциях.
 */
class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(result: Result) {
        when (result) {
            is Result.Success<*> -> {
                val cat = result.body as Cat
                setImage(
                    imageUrl = cat.imageUrl,
                    imageView = findViewById<ImageView>(R.id.image)
                )
                findViewById<TextView>(R.id.fact_textView).text = cat.fact
            }

            Result.Error.SocketError -> {
                val errorMessage = context.getString(R.string.no_internet_connection)
                showToast(errorMessage)
            }

            is Result.Error.OtherError -> {
                val errorMessage = result.e.message
                    ?: context.getString(R.string.unknown_error)
                showToast(errorMessage)
            }
        }
    }

    private fun setImage(imageUrl: String, imageView: ImageView) {
        val requiredSizeInPixels = dpToPixels(400F)
        Picasso.get()
            .load(imageUrl)
            .resize(requiredSizeInPixels, requiredSizeInPixels)
            .centerInside()
            .into(imageView)
    }

    private fun dpToPixels(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(result: Result)
}