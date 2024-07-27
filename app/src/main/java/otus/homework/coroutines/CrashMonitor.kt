package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(errorMsg: String?) {
        Log.w("CrashMonitor", errorMsg ?: "Something wrong happened")
    }
}