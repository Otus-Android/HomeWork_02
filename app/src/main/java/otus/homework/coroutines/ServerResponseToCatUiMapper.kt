package otus.homework.coroutines

fun mapServerResponseToCat(factResponse: FactResponse, imageResponse: ImageResponse) =
    Cat(fact = factResponse.fact, imageUrl = imageResponse.url)
