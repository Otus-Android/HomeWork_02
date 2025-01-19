package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class PresenterScope(coroutineContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = CoroutineName("PresenterCoroutine") +
            Dispatchers.Main +
            SupervisorJob() +
            CoroutineExceptionHandler { coroutineContext, throwable ->
                CrashMonitor.trackError(
                    throwable,
                    "Handled by CoroutineExceptionHandler $coroutineContext"
                )
            } +
            coroutineContext
}