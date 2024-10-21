package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val catFactsService by lazy { getRetrofit(CAT_FACT_BASE_URL).create(CatFactsService::class.java) }
    val imagesService by lazy { getRetrofit(IMAGE_BASE_URL).create(ImagesService::class.java) }

    private fun getRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        private const val CAT_FACT_BASE_URL = "https://catfact.ninja/"
        private const val IMAGE_BASE_URL = "https://api.thecatapi.com/"
    }
}