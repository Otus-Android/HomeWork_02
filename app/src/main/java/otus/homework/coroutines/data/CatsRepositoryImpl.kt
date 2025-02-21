package otus.homework.coroutines.data

import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import otus.homework.coroutines.domain.CatsRepository

class CatsRepositoryImpl : BaseRepository(), CatsRepository {

    override suspend fun getCatFact(): String {
        val result: Result<CatFactResponse> = handleApi {
            network.getCatFact().await()
        }
        return when (result) {
            is Result.Success -> result.data.fact
            is Result.Error -> throw result.exception
            }
        }

    override suspend fun getCatImage(): RequestCreator? {
        return Picasso.get().load(NetworkService.SOURCE_CATS_IMAGES)
    }
}