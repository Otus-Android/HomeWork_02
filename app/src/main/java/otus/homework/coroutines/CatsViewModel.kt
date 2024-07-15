package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

private const val VIEW_MODEL_CAT_JOB_KEY = "CatJob"
private const val VIEW_MODEL_AWAIT_TIMEOUT = 20000L

class CatsViewModel(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val jobs: MutableMap<JobKey, Job> = mutableMapOf(),
    /**
     * Должно быть только параметром, не переменной.
     * Локальная переменная создается дальше, чтобы ее можно было занулить
     */
    isDataLoadedCallbaсk: WeakReference<(Boolean) -> Unit>
) : ViewModel() {

    private var _catsView: ICatsView? = null
    private var isLogsEnabled = true

    /**
     * Локальная копия коллбэка для того,
     * чтобы занулить ее при необходимости
     * и минимизировать шанс утечки
     * */
    private var isDataLoadedCallback: ((Boolean) -> Unit)? = isDataLoadedCallbaсk.get()

    /**
     * Добавляю только нужное,
     * так как у ViewModelScope и так диспетчер Main.immediate и SupervisorJob
     */
    private val scopeContext: CoroutineContext by lazy {
        getCatsExceptionHandler() + getCatsCoroutineName()
    }

    fun onInitComplete() {
        /** Если корутина уже запущена, то ничего не делаем */
        if (jobs.getOrDefault(VIEW_MODEL_CAT_JOB_KEY, null)?.isActive == true) return

        /** Добавляем Job в мапу по ключу */
        jobs[VIEW_MODEL_CAT_JOB_KEY] = viewModelScope.launch(scopeContext) {
            val factResponseDeffered =
                viewModelScope.async(ioDispatcher) {
                    catFactService.getCatFact()
                }
            val imageResponseDeffered =
                viewModelScope.async(ioDispatcher) {
                    catImageService.getCatImage()
                }

            /** Timeout, чтобы не ждать ответа бесконечно */
            withTimeout(VIEW_MODEL_AWAIT_TIMEOUT) {
                val result = try {
                    /** Тестовое пробрасывание  SocketTimeoutException для проверки */
//                    throw SocketTimeoutException()

                    val factResponse = factResponseDeffered.await()
                        ?: return@withTimeout
                    val imageResponse = imageResponseDeffered.await()
                        ?: return@withTimeout
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
                 * Логика показа Тоста внутри View,
                 * в зависимости от результата
                 */
                _catsView?.populate(result)
                isDataLoadedCallback?.invoke(true)
            }
        }
    }

    fun cancelAllCoroutines() {
        viewModelScope.coroutineContext.cancelChildren()
        jobs.clear()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        isDataLoadedCallback = null
    }
}

class CatsViewModelFactory(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val isDataLoadedCallbaсk: WeakReference<(Boolean) -> Unit>
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CatsViewModel(
        catFactService = catFactService,
        catImageService = catImageService,
        isDataLoadedCallbaсk = isDataLoadedCallbaсk,
    ) as T
}