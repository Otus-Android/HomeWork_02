package otus.homework.coroutines

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ImageService {

    @GET("images/search")
    suspend fun getRandomImage(): Flow<Image>
}