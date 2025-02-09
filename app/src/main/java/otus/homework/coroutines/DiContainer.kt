package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitCatFacts by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactsService by lazy { retrofitCatFacts.create(CatsService::class.java) }

    private val retrofitCatImagesSource by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catImagesSourceService by lazy { retrofitCatImagesSource.create(CatImagesService::class.java) }
}