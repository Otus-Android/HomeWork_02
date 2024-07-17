package otus.homework.coroutines

import retrofit2.http.GET

interface CatsImageLinkService {
    @GET("v1/images/search")
    suspend fun getCatImageLink(): List<CatsImageLink>
}