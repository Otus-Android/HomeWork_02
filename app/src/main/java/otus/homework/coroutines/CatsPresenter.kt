package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) {

    private var _catsView: ICatsView? = null
    private var presenterScope: CoroutineScope? = null

    fun onInitComplete() {

        presenterScope?.launch {

            val factDeferred = async {

                try {
                    with(catsService.getCatFact()) {
                        when {
                            isSuccessful.not() || body() == null -> {
                                CrashMonitor.trackWarning()
                                null
                            }

                            else -> body()!!
                        }
                    }
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")
                    null
                } catch (ex: Exception) {
                    if (ex is CancellationException) throw ex
                    ex.message?.let { _catsView?.showToast(it) }
                    CrashMonitor.trackWarning()
                    null
                }
            }

            val pictureDeferred = async {

                try {
                    with(imagesService.getRandomImageUrl()) {
                        when {
                            isSuccessful.not() || body() == null -> {
                                CrashMonitor.trackWarning()
                                null
                            }

                            else -> body()!!
                        }
                    }
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервером")
                    null
                } catch (ex: Exception) {
                    if (ex is CancellationException) throw ex
                    ex.message?.let { _catsView?.showToast(it) }
                    CrashMonitor.trackWarning()
                    null
                }
            }

            val fact = factDeferred.await()
            val pictureUrl = pictureDeferred.await()?.first()?.url

            if (fact != null && pictureUrl != null) {
                _catsView?.populate(fact, pictureUrl)
            }
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
        presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    }

    fun detachView() {
        _catsView = null
        presenterScope?.cancel()
    }
}
