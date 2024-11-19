package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class PresenterScope(
    private val parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    private val dispatcher: MainCoroutineDispatcher = Dispatchers.Main,
): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job() + parentCoroutineContext + dispatcher + CoroutineName(DEFAULT_COROUTINE_NAME)

    companion object {
        private const val DEFAULT_COROUTINE_NAME = "CatsCoroutine"
    }
}