package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete(context: Context) {
        try {
            val response = catsService.getCatFact()
            _catsView?.populate(response)
        } catch (exception: SocketTimeoutException) {
            Toast.makeText(context, context.getString(R.string.could_not_get_response_from_server),
                Toast.LENGTH_SHORT).show()
        } catch (exception: Exception) {
            CrashMonitor.trackWarning()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}