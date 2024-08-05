package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface CatsPicturesService {

//    @GET("meow")
    @GET("floof")
    suspend fun getCatPicture() : Picture
}