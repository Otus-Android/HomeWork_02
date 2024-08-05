package otus.homework.coroutines

import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsPicturesService: CatsPicturesService,
    private val context: Context
) {

    private var _catsView: ICatsView? = null
    private val _presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        _presenterScope.launch {
            try {
                val fact = async<Fact> {
                    catsService.getCatFact()
                }

                val picture = async<Picture> {
                    catsPicturesService.getCatPicture()
                }

                val catModel = CatsModel(fact.await(), picture.await())
                _catsView?.populate(catModel)
            } catch (e : SocketTimeoutException) {
                _catsView?.toast(context.resources.getString(R.string.timeout_error))
            } catch (e : CancellationException) {
                throw e
            } catch (e : Exception) {
                CrashMonitor.trackWarning(e)
                _catsView?.toast(e.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _presenterScope.coroutineContext.cancelChildren()
        _catsView = null
    }
}

data class CatsModel(
    val fact: Fact,
    val picture: Picture
)