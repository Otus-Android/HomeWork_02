package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(warning:String?) {
        if (warning != null) {
            Log.e("CrashMonitor", warning)
        }
    }
}