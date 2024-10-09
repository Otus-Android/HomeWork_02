package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {
    val serviceCatFact: CatFactNinjaService
    val serviceCatImage: TheCatApiService

    class DiContainer : ApiService {
        private val retrofitCatFact by lazy {
            Retrofit.Builder()
                .baseUrl("https://catfact.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        override val serviceCatFact by lazy { retrofitCatFact.create(CatFactNinjaService::class.java) }

        private val retrofitCatImage by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        override val serviceCatImage by lazy { retrofitCatImage.create(TheCatApiService::class.java) }
    }
}

