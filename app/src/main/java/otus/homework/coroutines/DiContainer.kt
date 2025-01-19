package otus.homework.coroutines

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .client(OkHttpClient.Builder().apply {
                //для проверки работы при SocketTimeoutException
                readTimeout(5, TimeUnit.SECONDS)
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }
}