package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val catFactService by lazy {
        catFactRetrofit.create(CatFactService::class.java)
    }

    val catImageService by lazy {
        catImageRetrofit.create(CatImageService::class.java)
    }

    private val serviceBuilder by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val catFactRetrofit by lazy {
        serviceBuilder
            .baseUrl("https://catfact.ninja/")
            .build()
    }

    private val catImageRetrofit by lazy {
        serviceBuilder
            .baseUrl("https://api.thecatapi.com/v1/")
            .build()
    }
}