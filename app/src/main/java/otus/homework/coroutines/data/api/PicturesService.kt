package otus.homework.coroutines.data.api

import otus.homework.coroutines.data.model.Picture
import retrofit2.http.GET

interface PicturesService {
    @GET("/v1/images/search")
    suspend fun getPictures(): List<Picture>
}