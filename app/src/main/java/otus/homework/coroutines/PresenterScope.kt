package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

class PresenterScope : CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Main + CoroutineName("CatsCoroutine")

    fun cancelJobs() {
        job.cancelChildren()
    }
}