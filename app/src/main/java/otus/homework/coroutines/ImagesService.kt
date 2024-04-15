package otus.homework.coroutines

import retrofit2.http.GET

interface ImagesService {

    @GET("v1/images/search")
    suspend fun getImages() : List<Image>
}
