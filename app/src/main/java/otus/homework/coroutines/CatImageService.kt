package otus.homework.coroutines

import retrofit2.http.GET

interface CatImageService {

    @GET("images/search")
    suspend fun getCatImage(): List<ImageResponse?>?
}