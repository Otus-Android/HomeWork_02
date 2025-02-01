package otus.homework.coroutines

import otus.homework.coroutines.dto.Fact
import retrofit2.http.GET

interface CatsFactService {

    @GET("fact")
    suspend fun getCatFact() : Fact

}