package otus.homework.coroutines

import android.util.Log
import java.lang.RuntimeException

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e: Throwable) {
        Log.d("CrashMonitor", e.toString())
    }

    fun trackWarning(err: String) {
        Log.e("CrashMonitor", err)
    }
}