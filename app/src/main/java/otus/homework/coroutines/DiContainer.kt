package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitCats by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceCats: CatsService by lazy { retrofitCats.create(CatsService::class.java) }

    private val retrofitImages by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceImages: ImagesService by lazy { retrofitImages.create(ImagesService::class.java) }
}