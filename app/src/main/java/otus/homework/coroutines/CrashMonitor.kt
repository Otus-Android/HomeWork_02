package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    private const val TAG = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(
        message: String
    ) {
        Log.w(TAG, message)
    }
}