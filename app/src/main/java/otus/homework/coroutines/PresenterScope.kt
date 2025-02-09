package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope

class PresenterScope : CoroutineScope {
    private val job = Job()

    override val coroutineContext = job + Dispatchers.Main + CoroutineName("CatsCoroutine")

    fun clear() {
        job.cancel()
    }
}
