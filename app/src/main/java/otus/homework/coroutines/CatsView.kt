package otus.homework.coroutines

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import com.squareup.picasso.Picasso


class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: ICatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            // для проверки отмены корутин
            val switchActivityIntent = Intent(context, MainActivity2::class.java)
            startActivity(context, switchActivityIntent, bundleOf())
        }
    }

    override fun populate(fact: Fact, images: List<CatImage>) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        images.firstOrNull()?.let {
            Picasso.get().load(it.url).into(findViewById<ImageView>(R.id.cat_image))
        }
    }

    override fun populate(result: Result<CatData>) {
        when (result) {
            is Result.Success -> populate(result.data.fact, result.data.images)
            is Result.Error -> {
                when {
                    result.errorResId != null -> handle(result.errorResId)
                    result.errorMsg != null -> handle(result.errorMsg)
                    else -> handle(R.string.app_unknown_error)
                }
            }
        }
    }

    override fun handle(@StringRes resId: Int) {
        handle(context.getString(resId))
    }

    override fun handle(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView : IUserMessageHandler {

    fun populate(fact: Fact, images: List<CatImage>)

    fun populate(result: Result<CatData>)
}

sealed class Result<out T> {

    class Success<out T>(val data: T) : Result<T>()

    class Error(
        @StringRes val errorResId: Int? = null,
        val errorMsg: String? = null
    ) : Result<Nothing>()
}

data class CatData(
    val fact: Fact,
    val images: List<CatImage>,
)