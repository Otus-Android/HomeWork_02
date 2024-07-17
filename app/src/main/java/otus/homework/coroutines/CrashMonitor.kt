package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    private val tag = javaClass.simpleName

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String?) {
        Log.w(tag, "A crash has been detected: $message")
    }
}