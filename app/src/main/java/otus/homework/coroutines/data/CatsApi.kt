package otus.homework.coroutines.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CatsApi {

    @GET("fact")
    fun getCatFact(): Deferred<Response<CatFactResponse>>
}