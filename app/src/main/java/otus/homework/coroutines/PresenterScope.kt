package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope(context: CoroutineContext) : CoroutineScope {

    override val coroutineContext: CoroutineContext = context

    fun cancel() = coroutineContext.cancel()
}

val presenterScope: PresenterScope =
    PresenterScope(SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine"))