package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val fact: String,
	@field:SerializedName("length")
	val length: Int
)

data class ModelFact(
	val fact: Fact?,
	val imageCat: ImageCat?
)

data class ImageCat(
	@field:SerializedName("id")
	val id: String,
	@field:SerializedName("url")
	val url: String,
	@field:SerializedName("width")
	val width: Int,
	@field:SerializedName("height")
	val height: Int
)