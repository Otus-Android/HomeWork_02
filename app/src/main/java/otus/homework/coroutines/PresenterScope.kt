package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

class PresenterScope : CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext = job + Dispatchers.Main + CoroutineName("CatsCoroutine")

    fun cancelJobs() {
        job.cancelChildren()
    }
}
