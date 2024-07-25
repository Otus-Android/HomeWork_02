package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsViewModel(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val isDataLoadedCallbaсk: (Boolean) -> Unit
) : ViewModel() {

    private var _catsView: ICatsView? = null
    private var isLogsEnabled = true

    /**
     * Добавляю только нужное,
     * так как у ViewModelScope и так диспетчер Main.immediate и SupervisorJob
     */
    private val scopeContext: CoroutineContext by lazy {
        getCatsExceptionHandler() + getCatsCoroutineName()
    }

    fun onInitComplete() {
        viewModelScope.launch(scopeContext) {
            val factResponseDeffered =
                viewModelScope.async(ioDispatcher) {
                    catFactService.getCatFact()
                }
            val imageResponseDeffered =
                viewModelScope.async(ioDispatcher) {
                    catImageService.getCatImage()
                }

            val result = try {
                /** Тестовое пробрасывание  SocketTimeoutException для проверки */
//                    throw SocketTimeoutException()

                val factResponse = factResponseDeffered.await()
                val imageResponse = imageResponseDeffered.await()
                val imageResponseFirstElement = imageResponse.firstOrNull()
                    ?: return@launch

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

    fun cancelAllCoroutines() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}

class CatsViewModelFactory(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val isDataLoadedCallbaсk: (Boolean) -> Unit
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CatsViewModel(
        catFactService = catFactService,
        catImageService = catImageService,
        isDataLoadedCallbaсk = isDataLoadedCallbaсk,
    ) as T
}