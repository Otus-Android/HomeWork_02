package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun getRetrofit(url: String) =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    val service: CatsService by lazy { getRetrofit(FACT_URL).create(CatsService::class.java) }
    val imageService: CatsImageService by lazy { getRetrofit(IMAGE_URL).create(CatsImageService::class.java) }

    companion object {
        private const val FACT_URL = "https://catfact.ninja/"
        private const val IMAGE_URL = "https://api.thecatapi.com/v1/images/"
    }
}