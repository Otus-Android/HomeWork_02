package otus.homework.coroutines

import retrofit2.http.GET

interface CatImageService {

    @GET("v1/images/search")
    suspend fun getCatImages(): List<CatImage>
}