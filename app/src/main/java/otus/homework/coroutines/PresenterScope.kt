package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlin.coroutines.CoroutineContext

object PresenterScope : CoroutineScope, AutoCloseable {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main + job + CoroutineName("CatsCoroutine")
        }

    override fun close() {
        job.cancel()
    }
}