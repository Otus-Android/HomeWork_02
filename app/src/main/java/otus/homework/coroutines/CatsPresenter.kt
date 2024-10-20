package otus.homework.coroutines

import kotlinx.coroutines.coroutineScope

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() = coroutineScope {
        catsService.getCatFact().let { response ->
            if (response.isSuccessful && response.body() != null)
                _catsView?.populate(response.body()!!)
            else
                CrashMonitor.trackWarning()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}