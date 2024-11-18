package otus.homework.coroutines

import retrofit2.http.GET

interface CatFactService {

    @GET("fact")
    suspend fun getCatFact(): Fact
}