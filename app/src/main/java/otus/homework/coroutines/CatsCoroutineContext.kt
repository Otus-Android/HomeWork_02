package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlin.coroutines.CoroutineContext

/**
 * Вынесено отдельно, так как используется, как в презентере, так и во ViewModel
 */

fun getCoroutineContext(): CoroutineContext = getCatsExceptionHandler() + getCatsCoroutineName()

fun getCatsExceptionHandler() =
    CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e)
    }

fun getCatsCoroutineName() =
    CoroutineName("CatsCoroutine")