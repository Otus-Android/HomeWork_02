package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatsImageLink(
    @field:SerializedName("url")
    val url: String,
)
