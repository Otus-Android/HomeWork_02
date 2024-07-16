package otus.homework.coroutines

import retrofit2.http.GET

interface CatsPicturesService {

    @GET("v1/images/search")
    suspend fun getCatPicture(): List<Picture>

}