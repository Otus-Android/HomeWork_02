package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
)
