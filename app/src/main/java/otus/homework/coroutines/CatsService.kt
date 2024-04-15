package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET("fact")
    suspend fun getCatFact(): Fact

    @GET
    suspend fun getImage(@Url request: String = "https://api.thecatapi.com/v1/images/search"): List<CatImage>
}