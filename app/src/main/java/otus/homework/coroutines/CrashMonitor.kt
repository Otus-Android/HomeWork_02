package otus.homework.coroutines
import android.util.Log

object CrashMonitor {

    private final val TAG = javaClass.simpleName
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(text: String) {
        Log.e(TAG, text)
    }
}