package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

    @GET("https://api.thecatapi.com/v1/images/search")
    suspend fun getImage(): List<CatImage>
}