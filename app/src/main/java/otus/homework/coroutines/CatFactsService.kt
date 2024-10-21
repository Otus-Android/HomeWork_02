package otus.homework.coroutines

import retrofit2.http.GET

interface CatFactsService {

    @GET("fact")
    suspend fun getCatFact() : Fact
}