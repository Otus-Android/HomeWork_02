package otus.homework.coroutines.data.api

import otus.homework.coroutines.data.model.Fact
import retrofit2.Call
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    fun getCatFact() : Fact
}