package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.net.SocketTimeoutException

class CatsPresenter {

    private var _catsView: ICatsView? = null

    fun showCatFactAndPicture(cat: Cat) {
        _catsView?.populate(cat)
    }

    fun showOrLogError(exception: Throwable, context: Context) {
        if (exception is SocketTimeoutException) {
            Toast.makeText(context, context.getString(R.string.could_not_get_response_from_server),
                Toast.LENGTH_SHORT).show()
        }
    }

    fun showLoading() {
        _catsView?.showLoading()
    }

    fun setOnClickListener(onClick: () -> Unit) {
        Log.d("CatsPresenter.setOnClickListener", "onClick = '$onClick'")
        _catsView?.setButtonOnClickListener(onClick)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}
