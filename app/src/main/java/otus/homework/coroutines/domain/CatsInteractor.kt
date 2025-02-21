package otus.homework.coroutines.domain

import com.squareup.picasso.RequestCreator

class CatsInteractor(
    private val catsRepository: CatsRepository
) {
    suspend fun getCatFact(): String =
        catsRepository.getCatFact()

    suspend fun getCatImage(): RequestCreator? =
        catsRepository.getCatImage()
}