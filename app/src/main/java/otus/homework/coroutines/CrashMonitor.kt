package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(ex: String?) {
        Log.w("CrashMonitor",ex ?: "error" )
    }
}