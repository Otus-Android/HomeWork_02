package otus.homework.coroutines

import retrofit2.http.GET

interface PicsService {

    @GET("search")
    suspend fun getCatPicture(): List<CatPicture>
}
