package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.squareup.picasso.Picasso

//// реализация для mvp
//class CatsView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {
//
//    var presenter :CatsPresenter? = null
//
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
//        }
//    }
//
//    override fun populate(fact: Fact, imageUrl: String) {
//        findViewById<TextView>(R.id.fact_textView).text = fact.fact
//        findViewById<ImageView>(R.id.image).let {
//            if (imageUrl.isNotEmpty()) Picasso.get().load(imageUrl).into(it)
//        }
//    }
//
//    override fun showToast(text: String) {
//        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
//    }
//}
//
//interface ICatsView {
//
//    fun populate(fact: Fact, imageUrl: String)
//
//    fun showToast (text: String)
//}

// реализация для MVVM
class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView  {

    var presenter :CatsPresenter? = null
    lateinit var viewModel: CatsViewModel

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }

    override fun populate(fact: Fact, imageUrl: String) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        findViewById<ImageView>(R.id.image).let {
            if (imageUrl.isNotEmpty()) Picasso.get().load(imageUrl).into(it)
        }
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override suspend fun listenViewModel() {
        viewModel.contentState.collect { result ->
           when (result) {
               is Result.Error -> showToast(result.error)
               is Result.Success -> populate(result.fact, result.imageUrl)
           }
        }
    }
}

interface ICatsView {

    fun populate(fact: Fact, imageUrl: String)

    fun showToast (text: String)

    suspend fun listenViewModel()
}
