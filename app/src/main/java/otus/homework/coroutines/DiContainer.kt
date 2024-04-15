package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitCatFact by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitCatImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/images/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceCatFact: CatsService by lazy { retrofitCatFact.create(CatsService::class.java) }
    val serviceCatImage: CatsService by lazy { retrofitCatImage.create(CatsService::class.java) }
}