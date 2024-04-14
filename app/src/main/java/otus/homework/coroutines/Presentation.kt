package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Presentation(
  val url: String,
)

data class PresentationDTO(
  @field:SerializedName("id")
  val id: String,
  @field:SerializedName("url")
  val url: String,
)

fun PresentationDTO.toDomain() = Presentation(
  url = url,
)