package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.net.SocketTimeoutException

private const val PRESENTER_CAT_JOB_KEY = "CatJob"
private const val PRESENTER_AWAIT_TIMEOUT = 20000L

class CatsPresenter(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val scope: CoroutineScope = PresenterScope(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val jobs: MutableMap<JobKey, Job> = mutableMapOf(),
    private val isDataLoadedCallbaсk: (Boolean) -> Unit
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        /** Если корутина уже запущена, то ничего не делаем */
        if (jobs.getOrDefault(PRESENTER_CAT_JOB_KEY, null)?.isActive == true) return

        /** Добавляем Job в мапу по ключу */
        jobs[PRESENTER_CAT_JOB_KEY] = scope.launch {
            val factResponseDeffered = scope.async(ioDispatcher) {
                catFactService.getCatFact()
            }
            val imageResponseDeffered = scope.async(ioDispatcher) {
                catImageService.getCatImage()
            }

            /** Timeout, чтобы не ждать ответа бесконечно */
            withTimeout(PRESENTER_AWAIT_TIMEOUT) {
                val result = try {
                    /** Тестовое пробрасывание  SocketTimeoutException для проверки */
//                    throw SocketTimeoutException()

                    val factResponse = factResponseDeffered.await()
                    val imageResponse = imageResponseDeffered.await()
                    val imageResponseFirstElement = imageResponse.firstOrNull()
                        ?: return@withTimeout

                    val cat = mapServerResponseToCat(
                        factResponse = factResponse,
                        imageResponse = imageResponseFirstElement
                    )

                    Result.Success<Cat>(cat)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: SocketTimeoutException) {
                    Result.Error.SocketError
                } catch (e: Throwable) {
                    CrashMonitor.trackWarning(e)
                    Result.Error.OtherError(e)
                }

                /**
                 * Логика показа Тоста находится внутри View,
                 * работает в зависимости от результата
                 */
                _catsView?.populate(result)
                isDataLoadedCallbaсk(true)
            }
        }
    }

    fun cancelAllCoroutines() {
        scope.coroutineContext.cancelChildren()
        jobs.clear()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}