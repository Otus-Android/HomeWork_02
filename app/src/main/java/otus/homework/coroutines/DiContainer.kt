
    package otus.homework.coroutines
    import okhttp3.OkHttpClient
    import otus.homework.coroutines.data.api.CatsService
    import otus.homework.coroutines.data.api.PicturesService
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import java.util.concurrent.TimeUnit

    class DiContainer {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://catfact.ninja/")
                .client(OkHttpClient.Builder().apply {
                    readTimeout(5, TimeUnit.SECONDS)
                }.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private val retrofitImage by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val service by lazy { retrofit.create(CatsService::class.java) }

        val imageServise by lazy { retrofitImage.create(PicturesService::class.java) }
    }

