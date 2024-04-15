package otus.homework.coroutines

data class CatsUiState(
    val fact : String,
    val pictureUrl : String?,
)

class CatsUiStateMapper {

    fun map(fact : Fact, pictures : List<Picture>) : CatsUiState {
        return CatsUiState(
            fact = fact.fact,
            pictureUrl = pictures
                .firstOrNull { it.url.isNotBlank() }
                ?.url,
        )
    }
}
