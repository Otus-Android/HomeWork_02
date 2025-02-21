package otus.homework.coroutines

object Utils {
    inline fun log(block: () -> String) {
        println("[Thread: ${Thread.currentThread().name}]: " + block())
    }
}