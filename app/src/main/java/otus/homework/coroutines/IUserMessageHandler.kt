package otus.homework.coroutines

import androidx.annotation.StringRes

interface IUserMessageHandler {

    fun handle(@StringRes resId: Int)

    fun handle(msg: String)
}