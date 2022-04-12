package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class CatsPresenter(
    private val catsService: CatsService
) : BasePresenter<ICatsView>() {

    fun onInitComplete() {
        presenterScope.launch(CoroutineExceptionHandler { _, exception ->
            CrashMonitor.trackWarning()
        }) {
            try {
                view?.populate(
                    Cat(
                        fact = catsService.getCatFact(),
                        image = catsService.getCatImage()
                    )
                )
            } catch (exception: UnknownHostException) { //SocketTimeoutException
                view?.showNoConnectivityMessage()
            }
        }
    }
}