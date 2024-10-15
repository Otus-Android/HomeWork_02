package otus.homework.coroutines

import retrofit2.http.GET

interface CatsFacstService {
    @GET("fact")
    suspend fun getCatFact() : Fact
}