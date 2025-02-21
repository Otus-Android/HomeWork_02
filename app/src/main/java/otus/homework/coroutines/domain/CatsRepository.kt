package otus.homework.coroutines.domain

import com.squareup.picasso.RequestCreator

interface CatsRepository {
    suspend fun getCatFact(): String
    suspend fun getCatImage(): RequestCreator?
}