package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: PicsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete(context: Context) {
        try {
            val cat = Cat(catsService.getCatFact(), picsService.getCatPicture()[0])
            Log.d("onInitComplete", "cat = '$cat'")
            _catsView?.populate(cat)
        } catch (exception: SocketTimeoutException) {
            Toast.makeText(context, context.getString(R.string.could_not_get_response_from_server),
                Toast.LENGTH_SHORT).show()
        } catch (exception: Exception) {
            Log.d("onInitComplete", "Exception = ${exception}")
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
