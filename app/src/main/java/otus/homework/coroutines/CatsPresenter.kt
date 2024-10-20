package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private var context: Context? = null

    private val job = Job()
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + job + CoroutineName("CatsCoroutine"))

    fun onInitComplete() = presenterScope.launch {
        context = (_catsView as CatsView).context
        runCatching {
            catsService.getCatFact()
        }.mapCatching { response ->
            if (response.isSuccessful && response.body() != null)
                _catsView?.populate(response.body()!!)
            else
                CrashMonitor.trackWarning()
        }.getOrElse { e ->
            when (e) {
                is SocketTimeoutException -> {
                    Toast.makeText(
                        context,
                        context?.getString(R.string.socket_timeout_exception),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    CrashMonitor.trackWarning()
                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancel()
        _catsView = null
    }
}