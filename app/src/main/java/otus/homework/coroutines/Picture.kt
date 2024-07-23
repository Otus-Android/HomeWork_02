package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Picture(
	@field:SerializedName("image")
	val file: String,
	@field:SerializedName("link")
	val link: String
//	@field:SerializedName("file")
//	val file: String
)