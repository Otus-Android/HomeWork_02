package otus.homework.coroutines

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface ImageService {
    @GET("/v1/images/search")
    suspend fun getImage(): List<Image>
}

data class Image(
    @field: SerializedName("id")
    val id: String,
    @field: SerializedName("url")
    val url: String,
    @field: SerializedName("width")
    val width: Int,
    @field: SerializedName("height")
    val height: Int
)