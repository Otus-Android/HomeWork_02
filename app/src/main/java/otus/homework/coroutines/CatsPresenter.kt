package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val job = Job()
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + job + CoroutineName("CatsCoroutine"))

    fun onInitComplete() = presenterScope.launch {
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
        job.cancel()
        _catsView = null
    }
}