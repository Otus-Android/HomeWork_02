package otus.homework.coroutines.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {

    companion object {
//        const val SOURCE_CATS_IMAGES = "https://cataas.com/cat"
        const val SOURCE_CATS_IMAGES = "https://aws.random.cat/meow"
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    val createRetrofit : CatsApi by lazy { retrofit.create(CatsApi::class.java) }
}