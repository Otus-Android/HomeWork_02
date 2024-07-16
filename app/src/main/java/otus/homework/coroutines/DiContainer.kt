package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val catFactsRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CAT_FACTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catFactsService by lazy { catFactsRetrofit.create(CatsService::class.java) }

    private val catPicturesRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CAT_PICTURES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catPicturesService by lazy { catPicturesRetrofit.create(CatsPicturesService::class.java) }

    companion object {
        private const val CAT_FACTS_BASE_URL = "https://catfact.ninja/"
        private const val CAT_PICTURES_BASE_URL = "https://api.thecatapi.com/"
    }
}