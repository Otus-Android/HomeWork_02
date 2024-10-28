package otus.homework.coroutines.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsResult
import otus.homework.coroutines.CatsService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ImageResult

interface CatsImageLoader {
    fun load(): Flow<ImageResult>
}

interface CatsPresenter {
    fun loadFact()
    fun attachView(catsView: CatsView)
    fun detachView()
}

class CatsPresenterImpl(
    private val catsService: CatsService,
    private val imageLoader: CatsImageLoader,
): CatsPresenter {
    private var _catsView: CatsView? = null
    private var _scope: CoroutineScope? = null

    override fun loadFact() {
        _scope?.launch {
            try {
                val factJob = async { catsService.getCatFact() }
                val imageJob = async { imageLoader.load().single() }
                val factResponse = factJob.await()
                val imageResult = imageJob.await()
                if (!factResponse.isSuccessful) {
                    _catsView?.populate(CatsResult.Error("Failed to fetch cat fact"))
                } else if (imageResult is ImageResult.Error) {
                    _catsView?.populate(CatsResult.Error(imageResult.error))
                } else {
                    imageResult as ImageResult.Success
                    _catsView?.populate(
                        CatsResult.Success(
                            factResponse.body()!!.fact,
                            imageResult.image
                        )
                    )
                }
            } catch (e: Throwable) {
                CrashMonitor.trackWarning(e)
            }
        }
    }

    override fun attachView(catsView: CatsView) {
        _catsView = catsView
        _scope = PresenterScope("CatsCoroutine")
    }

    override fun detachView() {
        _scope?.cancel()
        _scope = null
        _catsView = null
    }
}