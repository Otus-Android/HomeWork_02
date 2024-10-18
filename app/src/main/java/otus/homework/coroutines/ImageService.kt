package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImageService {

    @GET("images/search")
    suspend fun getRandomImages(): Response<List<CatImage>>
}