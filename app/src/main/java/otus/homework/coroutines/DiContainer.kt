package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsFactRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsFactService by lazy { catsFactRetrofit.create(CatsFactService::class.java) }

    private val catsImagesRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsImagesService by lazy { catsImagesRetrofit.create(CatsImageService::class.java) }

}