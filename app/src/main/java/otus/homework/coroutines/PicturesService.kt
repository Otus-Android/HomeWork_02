package otus.homework.coroutines

import retrofit2.http.GET

interface PicturesService {
    @GET("v1/images/search")
    suspend fun getPictures() : List<Picture>
}
