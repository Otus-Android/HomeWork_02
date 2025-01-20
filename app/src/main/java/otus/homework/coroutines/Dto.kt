package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class FactDto(
    @SerializedName("fact") val fact: String,
    @SerializedName("length") val length: Int
)

data class ImageDto(
    @SerializedName("url") var url: String
)