package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(msg: String) {
        Log.d(MONITOR_TAG, msg)
    }

    fun trackError(t: Throwable? = null, msg: String? = null) {
        Log.d(MONITOR_TAG, msg, t)
    }

    private const val MONITOR_TAG = "OHC_CrashMonitor"
}