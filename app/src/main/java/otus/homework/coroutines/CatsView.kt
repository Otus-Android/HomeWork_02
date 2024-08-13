package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private var presenter :CatsPresenter? = null
    private var vm :CatsViewModel? = null

    fun setPresenter(presenter: CatsPresenter?){
        this.presenter = presenter
    }

    fun setViewModel(vm :CatsViewModel){
        this.vm = vm
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            vm?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        val factTextView = findViewById<TextView>(R.id.fact_textView)
        val imageView = findViewById<AppCompatImageView>(R.id.image)

        factTextView.text = catModel.textFact
        Picasso.get()
            .load(catModel.imageUrl)
            .into(imageView)
    }

    override fun showError(@StringRes res: Int) {
        showError(context.getString(res))
    }

    override fun showError(text: String?) {
        Toast.makeText(context, text ?: context.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(catModel: CatModel)
    fun showError(@StringRes res: Int)
    fun showError(text: String?)
}