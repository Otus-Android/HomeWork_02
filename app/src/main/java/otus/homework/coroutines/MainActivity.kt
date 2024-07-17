package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.service,
            diContainer.serviceCatsImageLink
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.getCatsViewData()
        }

        catsViewModel.catsLiveData.observe(this) {
            result ->
            when (result) {
                is Result.Success ->    view.populate(result.fact)
                is Result.Error   ->    view.showError(result.errorMessage)
            }
        }

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
	private fun buidlPresenter() {
		val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
		catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceCatsImageLink)
		view.presenter = catsPresenter
		catsPresenter.attachView(view)
		catsPresenter.onInitComplete()
	}
}