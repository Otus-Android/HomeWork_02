package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName

/**
 * Вынесено отдельно, так как используется, как в презентере, так и во ViewModel
 */

fun getCatsExceptionHandler() =
    CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e)
    }

fun getCatsCoroutineName() =
    CoroutineName("CatsCoroutine")