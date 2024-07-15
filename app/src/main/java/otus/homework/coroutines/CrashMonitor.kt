package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    private const val CRASH_MONITOR_TAG = "CoroutinesHomework: CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e: Throwable) {
        Log.d(CRASH_MONITOR_TAG, "trackWarning(): $e")
    }
}