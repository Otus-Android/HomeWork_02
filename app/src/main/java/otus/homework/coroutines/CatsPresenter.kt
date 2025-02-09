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
            loadCatFact()
            loadCatImage()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.clear()

    }

    private suspend fun loadCatFact() {
        try {
            // Переходим к фоновому потоку для выполнения запроса
            val fact = withContext(Dispatchers.IO) {
                catsService.getCatFact()
            }
            // Добавляем факт
            _catsView?.populate(fact)
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    // Показываем Toast для таймаута
                    Toast.makeText(
                        context,
                        "Не удалось получить ответ от сервером",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    // Логируем исключение и показываем сообщение для остальных случаев
                    CrashMonitor.trackWarning(e)
                    Toast.makeText(context, e.message ?: "Произошла ошибка", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private suspend fun loadCatImage() {
        Log.i("debug", "Pfuheprf")
        val catImageList = catImagesService.getCatImage()
        val imageUrl = catImageList.firstOrNull()?.url // Получение URL, если существует

        // Отрисовка изображения в UI-потоке
        if (imageUrl != null) {
            _catsView?.showCatImage(imageUrl) // Вызов метода для отображения изображения
        } else {
            // Обработайте ситуацию, когда URL не найден
            Toast.makeText(context, "Изображение не найдено", Toast.LENGTH_SHORT).show()
        }
    }
}

