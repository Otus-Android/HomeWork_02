package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Image(
	@field:SerializedName("url")
	val url: String
)