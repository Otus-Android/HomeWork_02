package otus.homework.coroutines

import retrofit2.http.GET

interface CatsService {
    @GET("https://catfact.ninja/fact")
    suspend fun getCatFact(): Fact

    @GET("https://api.thecatapi.com/v1/images/search")
    suspend fun getCatImage(): List<CatImage>
}