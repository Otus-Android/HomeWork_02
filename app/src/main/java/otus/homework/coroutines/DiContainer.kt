package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {
    private fun getRetrofit(baseUrl: String)=
         Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    val factsService by lazy { getRetrofit(FACTS_URL).create(CatsFacstService::class.java) }

    val imageService by lazy { getRetrofit(IMAGES_URL).create(CatsImagesService::class.java) }

    private companion object{
        const val FACTS_URL = "https://catfact.ninja/"
        const val IMAGES_URL = "https://api.thecatapi.com/"
    }
}