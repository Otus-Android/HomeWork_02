package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            // Описал причину такого безобразия в CatsService)
            .baseUrl(Endpoints.STUB)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    val repository: ICatsRepository by lazy {
        CatsRepositoryImpl(
            service = service,
        )
    }
}

object Endpoints {
    const val STUB = "https://stub.com"
    const val FACTS_BASE_URL: String = "https://catfact.ninja/"
    const val PRESENTATION_BASE_URL: String = "https://api.thecatapi.com/v1/images/search"
}