package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null

    private var jobCats: Job? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private fun <T> CoroutineScope.customAsync(block: suspend CoroutineScope.() -> T): Deferred<T?> {
        return async {
            try {
                block() ?: throw Exception()
            }catch (e: CancellationException){
                null
            }catch (e: SocketTimeoutException) {
                _catsView?.showError(R.string.error_server)
                null
            } catch (e: Exception) {
                _catsView?.showError(e.localizedMessage)
                CrashMonitor.trackWarning(e)
                null
            }
        }
    }

    fun onInitComplete() {
        jobCats?.cancel()
        jobCats = presenterScope.launch {

            val factDeferred = customAsync {
                val response = catsService.getCatFact()
                response.body()
            }

            val imageDeferred = customAsync {
                val response = catsService.getCatImages()
                response.body()
            }

            val fact = factDeferred.await()
            val image = imageDeferred.await()

            val model = CatModel(fact?.fact, image?.randomOrNull()?.url)
            _catsView?.populate(model)

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