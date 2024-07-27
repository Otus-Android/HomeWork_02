package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var jobPopulateCatsFacts: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("ERROR", exception.message.orEmpty())
        if (exception is java.net.SocketTimeoutException) {
            _catsView?.showError("Не удалось получить ответ от сервером")
        } else {
            _catsView?.showError(exception.localizedMessage)
            CrashMonitor.trackWarning(exception.message)
        }
    }

    fun populateData() {
        jobPopulateCatsFacts?.cancel()
        jobPopulateCatsFacts = presenterScope.launch(coroutineExceptionHandler) {
            val fact = async {
                val response = catsService.getCatFact()
                Log.d("FACT RESPONSE", response.toString())
                response.body()!!
            }
            val image = async {
                val response = imageService.getRandomImage()
                Log.d("IMAGE RESPONSE", response.toString())
                response.body()!!
            }
            val cat = CatModel(fact.await().fact, image.await()[0].url)
            Log.d("CAT", cat.toString())
            _catsView?.populate(cat)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        jobPopulateCatsFacts?.cancel()
        _catsView = null
    }
}