package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	val fact: String,
)

data class FactDTO(
	@field:SerializedName("fact")
	val fact: String,
	@field:SerializedName("length")
	val length: Int,
)
fun FactDTO.toDomain() = Fact(
	fact = fact,
)