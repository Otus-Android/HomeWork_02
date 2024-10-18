package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(error: Throwable) {
        Log.e(this.javaClass.name, error.message ?: "empty error")
    }
}