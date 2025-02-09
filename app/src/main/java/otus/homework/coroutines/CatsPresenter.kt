package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImagesService: CatImagesService,
    private val context: Context // Для показа Toast
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = loadCatFact()
                val imageUrl = loadCatImage()

                if (fact != null && imageUrl != null) {
                    val catModel = CatPresentationModel(fact.fact, imageUrl)
                    _catsView?.populate(catModel)
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.clear()

    }

    private suspend fun loadCatFact(): Fact? {
        return withContext(Dispatchers.IO) {
            catsService.getCatFact()
        }
    }

    private suspend fun loadCatImage(): String? {
        return withContext(Dispatchers.IO) {
            val catImageList = catImagesService.getCatImage()
            catImageList.firstOrNull()?.url // Возвращаем URL, если существует
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is SocketTimeoutException -> {
                Toast.makeText(context, "Не удалось получить ответ от сервером", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                CrashMonitor.trackWarning(e)
                Toast.makeText(context, e.message ?: "Произошла ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

