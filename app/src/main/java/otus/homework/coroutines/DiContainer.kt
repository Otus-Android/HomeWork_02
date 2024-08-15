package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun getRetrofit(url: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val catFactService by lazy { getRetrofit(URL_CAT_FACT).create(CatFactsService::class.java) }
    val catImageService by lazy { getRetrofit(URL_CAT_IMAGE).create(CatImageService::class.java) }

    companion object {
        private const val URL_CAT_FACT = "https://catfact.ninja/"
        private const val URL_CAT_IMAGE = "https://api.thecatapi.com/"
    }
}