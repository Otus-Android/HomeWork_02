package otus.homework.coroutines

enum class MyLinks {
    CATS_FACT,
    CATS_IMAGE;

    val url by lazy {
        when(this){
            CATS_FACT -> "https://catfact.ninja/fact"
            CATS_IMAGE -> "https://api.thecatapi.com/v1/images/search"
        }
    }
}