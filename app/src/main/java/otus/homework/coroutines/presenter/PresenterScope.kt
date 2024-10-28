package otus.homework.coroutines.presenter

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class PresenterScope(
    name: String,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob() + CoroutineName(name)
}
