package otus.homework.coroutines

import retrofit2.http.GET

private const val VERSION_API = "v1/"

interface CatImageService {

    @GET("${VERSION_API}images/search")
    suspend fun getRandomImages(): List<CatImage>
}