package otus.homework.coroutines

import retrofit2.http.GET

interface CatImagesService {
    @GET("v1/images/search/")
    suspend fun getCatImage(): List<CatImage>
}