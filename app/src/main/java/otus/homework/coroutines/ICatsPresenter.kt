package otus.homework.coroutines

import kotlinx.coroutines.Job

interface ICatsPresenter {
    var _catsView: ICatsView?
    var job: Job?

    fun onInitComplete()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        if (job?.isActive == true) job?.cancel()
    }

    companion object {
        internal const val COROUTINE_NAME = "CatsCoroutine"
    }
}