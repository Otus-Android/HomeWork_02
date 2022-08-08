package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET
    suspend fun getCatFact(@Url loginUrl: String) : Fact
}