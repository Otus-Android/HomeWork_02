package otus.homework.coroutines

import okhttp3.ResponseBody

sealed interface NetworkResult<T : Any> {

   class Success<T : Any>(val data: T) : NetworkResult<T>
   class Error<T : Any>(val error: ResponseBody?) : NetworkResult<T>
   class Exception<T : Any>(val e: Throwable) : NetworkResult<T>
}