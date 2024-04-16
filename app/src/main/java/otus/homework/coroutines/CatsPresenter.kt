package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageCatService: ImageCatService
) {

    private var _catsView: ICatsView? = null
    private var error = false

    private val presenterScope = CoroutineScope(Dispatchers.Main.immediate)

    fun onInitComplete() {
        val fact = presenterScope.async { catsService.getCatFact() }
        val image = presenterScope.async { imageCatService.getCatImage() }
        presenterScope.launch {
            try {
                val responseFact = fact.await()
                val responseImage = image.await()
                if (responseFact.isSuccessful && responseFact.body() != null
                    && responseImage.isSuccessful && responseImage.body() != null) {
                    error = false
                    _catsView?.populate(ModelPresentation(responseFact.body()!!, responseImage.body()!!.first()))
                } else {error = true}
            } catch (s: SocketTimeoutException) {
                error = true
            } catch (e: Exception) {
                error = true
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
