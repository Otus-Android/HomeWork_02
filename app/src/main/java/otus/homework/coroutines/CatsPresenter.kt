package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    fun onInitComplete() {
        job?.cancel()
        job = presenterScope.launch {
            try {
                var fact: Fact?
                withContext(Dispatchers.IO) {
                    fact = catsService.getCatFact()
                }
                fact?.let {
                    _catsView?.populate(it)
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showError()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
        job = null
    }
}