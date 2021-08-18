package otus.homework.coroutines.retrofit

import otus.homework.coroutines.model.Fact
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact() : Fact
}