package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    private var _catsView: ICatsView? = null

    private val presenterScope =
        CoroutineScope(Job() + Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            val fact = async { catsService.getCatFact() }
            val pictures = async { catsService.getRandomPictures() }

            val result = try {
                Result.Success<CatInfo>(
                    catInfo = CatInfo(
                        fact = fact.await(),
                        url = pictures.await().firstOrNull()
                    )
                )
            } catch (e: SocketTimeoutException) {
                Result.Error(e)
            } catch (e: CancellationException) {
                Result.Error(e)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                Result.Error(e)
            }
            _catsView?.populate(result)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}