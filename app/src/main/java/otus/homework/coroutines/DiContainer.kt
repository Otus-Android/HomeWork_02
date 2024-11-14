package otus.homework.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val provideRemoteBuildClient: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(provideLoggingInterceptor()).build()


    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .client(provideRemoteBuildClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    private val retrofitImage by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .client(provideRemoteBuildClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceImage by lazy { retrofitImage.create(ImageService::class.java) }
}