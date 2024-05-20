package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

data class Pic(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("url")
    val url: String
)
