package otus.homework.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CatsPresenter(
    private val catsService: CatsService
) {

    suspend fun onPopulateFact(): Flow<Fact> = catsService.getCatFact()
}