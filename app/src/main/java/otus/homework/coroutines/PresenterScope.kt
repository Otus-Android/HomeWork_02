package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class PresenterScope(
    private val parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    private val dispatcher: MainCoroutineDispatcher = Dispatchers.Main,
): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = parentCoroutineContext + dispatcher + CoroutineName(DEFAULT_COROUTINE_NAME)

    companion object {
        private const val DEFAULT_COROUTINE_NAME = "CatsCoroutine"
    }
}