package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsImageService {
    @GET("search")
    suspend fun getCatImage() : Response<List<CatImage>>
}