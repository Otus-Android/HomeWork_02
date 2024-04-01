package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImage: CatsImage,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    fun getCatData() {
        presenterScope.launch {
            runCatching {
                val catData = async {
                    CatData(
                        catsService.getCatFact(),
                        catsImage.getCatImage().firstOrNull()
                    )
                }
                catData.await().apply {
                    _catsView?.populateFact(fact)
                    image?.url?.let {
                        _catsView?.populateImage(it)
                    }
                }
            }.onFailure { throwable ->
                CrashMonitor.trackWarning()
            }
        }

    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}