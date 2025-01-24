package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val text: String,
	val length: Int
)
