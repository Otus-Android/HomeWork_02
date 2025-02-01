package otus.homework.coroutines

import otus.homework.coroutines.dto.CatImage
import retrofit2.http.GET

interface CatsImageService {

    @GET("v1/images/search")
    suspend fun getCatImage() : List<CatImage>

}