package otus.homework.coroutines.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import otus.homework.coroutines.CrashMonitor
import kotlin.coroutines.CoroutineContext

class ViewModelCoroutineScope(
    private val context: CoroutineContext = Dispatchers.Main.immediate + SupervisorJob() + defaultExceptionsHandler
): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = context

    companion object {
        private val defaultExceptionsHandler = CoroutineExceptionHandler { _, error ->
            CrashMonitor.trackWarning(error)
        }
    }
}