package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder()
                .addInterceptor( HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
                .build())
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    private val retrofit4Pictures by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder()
                .addInterceptor( HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
                .build())
//            .baseUrl("https://aws.random.cat/") // котики не работают (
            .baseUrl("https://randomfox.ca/") // вместо них пусть будут лисички )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val picturesService by lazy { retrofit4Pictures.create(CatsPicturesService::class.java) }
}