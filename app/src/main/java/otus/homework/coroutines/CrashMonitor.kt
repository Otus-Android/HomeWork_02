package otus.homework.coroutines

import android.util.Log
import okhttp3.ResponseBody

private const val API_ERROR = "API_ERROR"

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(error: Throwable) {
        Log.e(this.javaClass.name, error.message ?: "empty error")
    }

    fun trackApiError(errorBody: ResponseBody?) {
        Log.e(API_ERROR + this.javaClass.name, errorBody?.string() ?: "empty error body")
    }
}