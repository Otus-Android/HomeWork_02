package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class CatsCoroutineScope() : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        CoroutineName("CatsCoroutine")
}