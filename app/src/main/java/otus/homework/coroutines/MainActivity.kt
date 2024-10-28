package otus.homework.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import otus.homework.coroutines.presenter.CatsPresenter
import otus.homework.coroutines.presenter.CatsView
import otus.homework.coroutines.presenter.PresenterActivity
import otus.homework.coroutines.viewmodel.ViewModelActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val imageLoader: ImageLoader = ImageLoaderImpl()
    private lateinit var catsView: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)

        findViewById<Button>(R.id.open_presenter_button).setOnClickListener {
            val intent = Intent(this, PresenterActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.open_viewmodel_button).setOnClickListener {
            val intent = Intent(this, ViewModelActivity::class.java)
            startActivity(intent)
        }
    }
}