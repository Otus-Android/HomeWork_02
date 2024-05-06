package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PresenterScope : CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.Main + CoroutineName("CatsCoroutine")
}