package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val catsPresenter1 = CatsPresenter(diContainer.service,diContainer.imageService)

        catsPresenter = catsPresenter1
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        //catsPresenter.onInitComplete()

        viewModel = CatsViewModel(diContainer.service,diContainer.imageService)
        view.viewModel = viewModel
        viewModel.catsLiveData.observe(this) { result ->
            if (result is Success){
                view.populate(result.cat)
            } else if (result is Error) {
                view.showToast(result.error)
            }
        }
        viewModel.init()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}