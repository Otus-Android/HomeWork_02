package otus.homework.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    /** Выбрать, работать через viewModel или presenter */
    private val isViewModel = true

    private val diContainer = DiContainer()

    lateinit var catsPresenter: CatsPresenter

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            catFactService = diContainer.catFactService,
            catImageService = diContainer.catImageService,
            isDataLoadedCallbaсk = isDataLoadedCallback()
        )
    }

    private var isDataLoaded = false

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        if (isViewModel) {
            view.viewModel = catsViewModel
            catsViewModel.attachView(view)
        } else {
            catsPresenter = CatsPresenter(
                catFactService = diContainer.catFactService,
                catImageService = diContainer.catImageService,
                isDataLoadedCallbaсk = isDataLoadedCallback()
            )
            view.presenter = catsPresenter
            catsPresenter.attachView(view)
        }
    }

    /**
     * Перенес загрузку данных в метод onStart(),
     * так как были кейсы блокировки экрана перед первой загрузкой.
     * Из-за того, что корутины в onStop() останавливаются,
     * в onStart() пользователь может увидеть пустой экран
     */
    override fun onStart() {
        super.onStart()
        if (!isDataLoaded) {
            if (isViewModel) {
                catsViewModel.onInitComplete()
            } else {
                catsPresenter.onInitComplete()
            }
        }
    }

    /**
     * Колбек для того,чтобы установить флаг idDataLoaded на true
     * в корутине загрузки данных.
     * Установить его в onStart() не получится, так как корутина может оборваться,
     * а флаг будет все равно установлен
     */
    private fun isDataLoadedCallback(): (Boolean) -> Unit = { isLoaded: Boolean ->
        isDataLoaded = isLoaded
    }

    override fun onStop() {
        if (isViewModel) {
            if (isFinishing) {
                catsViewModel.detachView()
            }
        } else {
            if (isFinishing) {
                catsPresenter.detachView()
            }
        }
        super.onStop()
    }
}