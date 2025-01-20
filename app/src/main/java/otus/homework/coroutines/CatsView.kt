package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val viewScope by lazy { CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine")) }

    private lateinit var catsViewModel: CatsViewModel

    private var textView: TextView? = null
    private var imageView: ImageView? = null

    fun bindViewModel(catsViewModel: CatsViewModel) {
        this.catsViewModel = catsViewModel

        viewScope.launch {
            catsViewModel.state.collectLatest { result ->
                when (result) {
                    is Result.Success<*> -> {
                        if (result.data is Model) {
                            if (result.data != Model.EMPTY_MODEL) {
                                imageView?.let {
                                    Picasso.get()
                                        .load(result.data.imageUrl)
                                        .into(it)
                                }
                                textView?.let {
                                    it.text = result.data.fact.fact
                                }
                            }
                        }
                    }
                    is Result.Error -> { }
                }
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById(R.id.fact_textView)
        imageView = findViewById(R.id.image)
        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.getContent()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        catsViewModel.state.value
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope.cancel()
    }
}

data class Model(
    val fact: Fact,
    val imageUrl: String
) {
    companion object {
        val EMPTY_MODEL = Model(
            fact = Fact(fact = "", length = 0),
            imageUrl = ""
        )
    }
}