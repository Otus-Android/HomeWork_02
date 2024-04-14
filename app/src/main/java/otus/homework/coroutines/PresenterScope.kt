package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + CoroutineName(NAME)

  companion object {
    const val NAME = "CatsCoroutine"
  }
}