package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatImage(
    @SerializedName("id")
    val id: String,

    @SerializedName("url")
    val url: String,
)