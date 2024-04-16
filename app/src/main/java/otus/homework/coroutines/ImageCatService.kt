package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET

interface ImageCatService {

    @GET("search")
    suspend fun getCatImage() : Response<List<ImageCat>>
}
