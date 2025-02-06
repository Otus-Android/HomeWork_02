package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val context: Context // Для показа Toast
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
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
                        Toast.makeText(context, "Не удалось получить ответ от сервером", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Логируем исключение и показываем сообщение для остальных случаев
                        CrashMonitor.trackWarning(e)
                        Toast.makeText(context, e.message ?: "Произошла ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
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
