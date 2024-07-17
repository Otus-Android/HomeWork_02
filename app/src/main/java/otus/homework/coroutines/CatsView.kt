package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var viewModel: CatFactViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        findViewById<Button>(R.id.button).setOnClickListener {

            when (hwTaskFeature) {
                FeatureFlag.HW_TASK_1_CAT_PRESENTER -> {
                    presenter?.onInitComplete()
                }

                FeatureFlag.HW_TASK_3_CAT_VIEW_MODEL -> {
                    viewModel?.fetchCatData()
                }
            }
        }
    }

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact
        val imageView = findViewById<android.widget.ImageView>(R.id.cat_image)
        Picasso.get().load(catData.pictureUrl).into(imageView)
    }
}

interface ICatsView {

    fun populate(catData: CatData)
}