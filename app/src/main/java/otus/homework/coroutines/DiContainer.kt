package otus.homework.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.job
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class DiContainer(private val lifecycle: Lifecycle) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }

    val presenterScope by lazy {
        val mainDispatcher = Executors
            .newSingleThreadExecutor()
            .asCoroutineDispatcher()

        CoroutineScope(CoroutineName("CatsCoroutine") + mainDispatcher + lifecycle.coroutineScope.coroutineContext.job)
    }
}