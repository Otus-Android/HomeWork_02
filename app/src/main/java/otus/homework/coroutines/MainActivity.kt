package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ConnectionErrorHandler {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatFactViewModel by viewModels<CatFactViewModel> {
        CatsViewModelFactory(diContainer.catFactsService, diContainer.catPicturesService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            diContainer.catFactsService, diContainer.catPicturesService, this
        )

        when (hwTaskFeature) {
            FeatureFlag.HW_TASK_1_CAT_PRESENTER -> {
                view.presenter = catsPresenter
                catsPresenter.attachView(view)
                catsPresenter.onInitComplete()
            }

            FeatureFlag.HW_TASK_3_CAT_VIEW_MODEL -> {
                view.viewModel = viewModel
                observeCatsData(view)
                viewModel.fetchCatData()
            }
        }
    }

    private fun observeCatsData(view: CatsView) {
        viewModel.catsLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error.ServerError -> onError()
                is Result.Error.GeneralError -> CrashMonitor.trackWarning(result.exception.message)
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()

        when (hwTaskFeature) {
            FeatureFlag.HW_TASK_3_CAT_VIEW_MODEL -> viewModel.cancelFetch()
            FeatureFlag.HW_TASK_1_CAT_PRESENTER -> catsPresenter.cancelFetch()
        }
    }

    override fun onError() {
        Toast.makeText(
            this,
            getString(R.string.toast_failed_to_get_server_response),
            Toast.LENGTH_LONG
        ).show()
    }
}