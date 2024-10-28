package otus.homework.coroutines

import android.graphics.Bitmap

sealed interface CatsResult {
    object Init : CatsResult

    data class Success(val fact: String, val image: Bitmap) : CatsResult
    data class Error(val message: String) : CatsResult
}