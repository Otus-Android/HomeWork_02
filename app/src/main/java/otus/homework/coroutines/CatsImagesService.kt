package otus.homework.coroutines

import retrofit2.http.GET

interface CatsImagesService {
    @GET("v1/images/search")
    suspend fun getImage(): List<Image>
}