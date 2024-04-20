package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatFact(
	@field:SerializedName("fact")
	val fact: String,
	@field:SerializedName("length")
	val length: Int
)

data class CatImage(
	@field:SerializedName("id") val id: String,
	@field:SerializedName("url") val url: String
)

val emptyCatData = CatData("", "")

data class CatData(
	val fact: String,
	val imageUrl: String
)