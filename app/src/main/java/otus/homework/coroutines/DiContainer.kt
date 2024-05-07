package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactService by lazy { createRetrofit("https://catfact.ninja/").create(CatsService::class.java) }
    val catImageService by lazy { createRetrofit("https://api.thecatapi.com/v1/images/").create(CatImageService::class.java) }
}