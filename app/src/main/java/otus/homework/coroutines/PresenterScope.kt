package otus.homework.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PresenterScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    context: CoroutineName = CoroutineName("CatsCoroutine")
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = dispatcher + context
}
