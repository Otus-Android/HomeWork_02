package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catsFactClient by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val catsImageClient by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsFactService: CatsFactService by lazy { catsFactClient.create(CatsFactService::class.java) }

    val catsImageService: CatsImageService by lazy { catsImageClient.create(CatsImageService::class.java) }
}
