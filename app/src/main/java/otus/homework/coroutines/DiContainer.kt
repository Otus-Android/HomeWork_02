package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getCatsService(): CatsService {
        return getRetrofit().create(CatsService::class.java) as CatsService
    }
}