package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImageService {

    @GET("v1/images/search")
    suspend fun getImage(): Response<List<ImageModel>>
}