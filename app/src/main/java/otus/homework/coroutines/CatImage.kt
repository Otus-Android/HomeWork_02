package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatImage(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("url") val url: String
)