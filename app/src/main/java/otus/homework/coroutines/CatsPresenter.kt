package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val context: Context,
    private val catsRepository: ICatsRepository,
) {
    private var _catsJob: Job? = null
    private var _catsView: ICatsView? = null

    private val scope: CoroutineScope by lazy { PresenterScope() }

    fun onInitComplete() {
        cancelCatJob()
        _catsJob = scope.launch {
            try {
                val cat: Cat = catsRepository.getCat()

                _catsView?.populate(cat)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(
                    context,
                    "Не удалось получить ответ от сервера",
                    Toast.LENGTH_SHORT,
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                CrashMonitor.trackWarning()
                e.message?.let { eMessage ->
                    Toast.makeText(
                        context,
                        eMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
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

    fun dispose() {
        cancelCatJob()
    }

    private fun cancelCatJob() {
        _catsJob?.cancel()
        _catsJob = null
    }
}