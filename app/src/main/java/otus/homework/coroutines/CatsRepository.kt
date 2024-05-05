package otus.homework.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface ICatsRepository {
  suspend fun getCatFact(): Fact
  suspend fun getCatPresentation(): Presentation
}

class CatsRepositoryImpl(
  private val ioDispatcher: CoroutineDispatcher,
  private val service: CatsService,
) : ICatsRepository {
  override suspend fun getCatFact(): Fact = withContext(ioDispatcher) {
    service.getCatFact(Endpoints.FACTS_BASE_URL + "fact").toDomain()
  }

  override suspend fun getCatPresentation(): Presentation =
    withContext(ioDispatcher) {
      service.getCatPresentation(Endpoints.PRESENTATION_BASE_URL)
        .firstOrNull()?.toDomain()
        ?: throw Exception("first presentation not found")
    }
}