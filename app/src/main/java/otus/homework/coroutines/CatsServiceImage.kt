package otus.homework.coroutines

import retrofit2.Call
import retrofit2.http.GET

interface CatsServiceImage {

    @GET("v1/images/search")
    suspend fun getCatImage() : Array<CatImage>
}