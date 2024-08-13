package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ImageModel(
    @SerializedName("url") val url: String?
)