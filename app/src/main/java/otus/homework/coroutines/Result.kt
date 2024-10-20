package otus.homework.coroutines

import kotlin.Result

sealed interface Result<T> {
    data class Success<T>(val mainUiModel: T): otus.homework.coroutines.Result<T>
    data class Error<T>(val error: Throwable): otus.homework.coroutines.Result<T>
    class Noting<T>: otus.homework.coroutines.Result<T>
}