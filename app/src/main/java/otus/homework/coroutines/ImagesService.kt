package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImagesService {

    @GET("search")
    suspend fun getRandomImageUrl() : Response<List<Image>>


}