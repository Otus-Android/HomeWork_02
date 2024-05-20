package otus.homework.coroutines

import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Pic
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Fact

    @GET("https://api.thecatapi.com/v1/images/search")
    suspend fun getCatPic() : List<Pic>
}