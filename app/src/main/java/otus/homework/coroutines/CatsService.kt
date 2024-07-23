package otus.homework.coroutines

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Flow<Fact>
}