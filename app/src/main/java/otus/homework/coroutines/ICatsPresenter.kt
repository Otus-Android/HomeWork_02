package otus.homework.coroutines

import kotlinx.coroutines.Job

interface ICatsRefresh{
    fun onInitComplete()
}

interface ICatsPresenter : ICatsRefresh {
    var _catsView: ICatsView?
    var job: Job?

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