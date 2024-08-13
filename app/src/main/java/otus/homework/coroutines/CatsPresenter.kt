package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null

    private var jobCats: Job? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        jobCats?.cancel()
        jobCats = presenterScope.launch {
            val fact = async {
                val response = catsService.getCatFact()
                response.body()!!
            }
            val image = async {
                val response = catsService.getCatImages()
                response.body()!!
            }
            try {
                val model = CatModel(fact.await().fact, image.await().randomOrNull()?.url)
                _catsView?.populate(model)
            }catch (e: SocketTimeoutException){
                _catsView?.showError(R.string.error_server)
            }catch (exception: Exception){
                _catsView?.showError(exception.localizedMessage)
                CrashMonitor.trackWarning(exception)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        jobCats?.cancel()
        jobCats = null
        _catsView = null
    }
}