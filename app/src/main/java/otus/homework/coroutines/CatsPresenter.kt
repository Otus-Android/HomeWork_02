package otus.homework.coroutines

import kotlinx.coroutines.flow.Flow

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
) {

    suspend fun populateFact(): Flow<Fact> = catsService.getCatFact()

    suspend fun populateRandomImage(): Flow<Image> = imageService.getRandomImage()
}