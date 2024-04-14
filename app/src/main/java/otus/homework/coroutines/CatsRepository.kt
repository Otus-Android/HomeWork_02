package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface ICatsRepository {
  suspend fun getCat(): Cat
}

class CatsRepositoryImpl(
  private val service: CatsService,
) : ICatsRepository {
  override suspend fun getCat(): Cat = withContext(Dispatchers.IO) {
    val defFact = async {
      getCatFact()
    }
    val defPresentation = async {
      getCatPresentation()
    }
    val (fact, presentation) = Pair(defFact.await(), defPresentation.await())
    Cat(
      fact = fact,
      presentation = presentation,
    )
  }

  private suspend fun getCatFact(): Fact {
    return service.getCatFact(Endpoints.FACTS_BASE_URL + "fact").toDomain()
  }

  private suspend fun getCatPresentation(): Presentation {
    return service.getCatPresentation(Endpoints.PRESENTATION_BASE_URL)
      .firstOrNull()?.toDomain()
      ?: throw Exception("first presentation not found")
  }
}