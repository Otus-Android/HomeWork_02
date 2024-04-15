package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofitBuilder by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
    }

    val service by lazy {
        retrofitBuilder
            .baseUrl("https://catfact.ninja/")
            .build()
            .create(CatsService::class.java)
    }

    val pictureService by lazy {
        retrofitBuilder
            .baseUrl("https://api.thecatapi.com/")
            .build()
            .create(PicturesService::class.java)
    }

    val catsUiStateMapper by lazy { CatsUiStateMapper() }
}