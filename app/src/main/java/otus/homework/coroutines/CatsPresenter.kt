package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.dto.CatImage


class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImagesService: CatsImageService
): CatLoader {

    private var _catsView: ICatsView? = null

    override fun onInitComplete() {

        PresenterScope.launch {
            try {

                val catFact = async { catsFactService.getCatFact() }.await()

                val catImage: List<CatImage> = async { catsImagesService.getCatImage() }.await()

                _catsView?.populate(catFact, catImage.first())
            } catch (ex: Exception) {
                if (ex is java.net.SocketTimeoutException) {
                    _catsView?.onError("Unable to get response from server")
                }
                CrashMonitor.trackWarning(ex.message!!)
            }
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}