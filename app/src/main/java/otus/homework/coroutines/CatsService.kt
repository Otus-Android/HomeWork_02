package otus.homework.coroutines

import retrofit2.http.GET

interface CatFactNinjaService {
    @GET("fact")
    suspend fun getCatFact(): CatFact
}

interface TheCatApiService {
    @GET("images/search")
    suspend fun getCatImage(): List<CatImage>
}