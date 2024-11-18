package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun retrofit(url: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val serviceCatFact: CatFactService by lazy { retrofit(FACT).create(CatFactService::class.java) }
    val serviceCatImage: CatImageService by lazy { retrofit(IMAGE).create(CatImageService::class.java) }

    companion object {
        private const val FACT = "https://catfact.ninja/"
        private const val IMAGE = "https://api.thecatapi.com/"
    }
}