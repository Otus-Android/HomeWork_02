package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsRepository: ICatsRepository,
) {
    private var _catsJob: Job? = null
    private var _catsView: ICatsView? = null

    private val scope: CoroutineScope = PresenterScope()

    fun onInitComplete() {
        cancelCatJob()
        _catsJob = scope.launch {
            try {
                val cat: Cat = loadCat()

                _catsView?.populate(cat)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }

                e.printStackTrace()
                CrashMonitor.trackWarning()
                e.message?.let { eMessage ->
                    _catsView?.showToast(eMessage)
                }
            }
        }
    }

    private suspend fun loadCat(): Cat = coroutineScope {
        val defFact = async {
            catsRepository.getCatFact()
        }
        val defPresentation = async {
            catsRepository.getCatPresentation()
        }
        val (fact, presentation) = Pair(defFact.await(), defPresentation.await())

        Cat(
            fact = fact,
            presentation = presentation,
        )
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        cancelCatJob()
        _catsView = null
    }

    private fun cancelCatJob() {
        _catsJob?.cancel()
        _catsJob = null
    }
}