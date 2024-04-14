package otus.homework.coroutines

import retrofit2.http.GET
import retrofit2.http.Url

interface CatsService {
    // @Url так как разные домены у поставщиков для фактов и изображений
    // как вариант, ещё можно было сделать два отдельных сервиса
    // Например: CatFactsService, CatPresentationService (для каждого свой экземпляр retrofit)
    @GET
    suspend fun getCatFact(@Url url: String) : FactDTO

    @GET
    suspend fun getCatPresentation(@Url url: String) : List<PresentationDTO>
}