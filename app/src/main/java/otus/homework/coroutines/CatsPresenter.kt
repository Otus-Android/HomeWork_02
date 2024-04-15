package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(
        context = Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDeferred = async { getFact() }
                val imageDeferred = async { getFirstImage() }

                val cat = getPresentationCat(factDeferred.await(), imageDeferred.await())
                _catsView?.populate(cat)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast(ExceptionType.SocketTimeout)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                if (e.message != null ) _catsView?.showToast(ExceptionType.Other(e.message!!))
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

    private suspend fun getFact(): Fact {
        var fact: Fact
        withContext(Dispatchers.IO) {
            fact = catsService.getCatFact()
        }
        return fact
    }

    private suspend fun getFirstImage(): Image {
        var image: Image
        withContext(Dispatchers.IO) {
            image = imagesService.getImages().first()
        }
        return image
    }

    private fun getPresentationCat(fact: Fact, image: Image) =
        CatPresentation(
            fact = fact.fact,
            imageUrl = image.url
        )
}

sealed class ExceptionType {
    object SocketTimeout : ExceptionType()
    data class Other(val message: String): ExceptionType()
}
