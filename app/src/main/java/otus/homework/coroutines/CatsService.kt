package otus.homework.coroutines

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {

    @GET
    suspend fun getCatFact(@Url url: String = MyLinks.CATS_FACT.url) : Response<Fact>

    @GET
    suspend fun getCatImages(@Url url: String = MyLinks.CATS_IMAGE.url) : Response<List<ImageModel>>
}