package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class PresenterScope(override val coroutineContext: CoroutineContext = CoroutineName("CatsCoroutine") + Dispatchers.Main) : CoroutineScope {
}