package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        Utils.log { "[CrashMonitor]: $message" }
    }
}