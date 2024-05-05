    package otus.homework.coroutines

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import kotlinx.coroutines.CoroutineExceptionHandler
    import kotlinx.coroutines.async
    import kotlinx.coroutines.launch
    import java.net.SocketTimeoutException
    import kotlin.coroutines.cancellation.CancellationException

    class CatsViewModel(
        private val diContainer: DiContainer
    ): ViewModel() {
        private val _catsInfo = MutableLiveData<Result<CatInfo>>()
        val catsInfo: LiveData<Result<CatInfo>> = _catsInfo

        private val crashMonitor = CrashMonitor

        private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            crashMonitor.trackException(throwable)
            _catsInfo.value = Result.Error("coroutineExceptionHandler")
        }

        fun getCatInfo() {
            viewModelScope.launch(coroutineExceptionHandler) {
                try {
                    val factDeferred = async { diContainer.service.getCatFact() }
                    val imageDeferred = async { diContainer.catImageService.getCatImage() }

                    val factResponse = factDeferred.await()
                    val imageResponse = imageDeferred.await()

                    if (factResponse.isSuccessful) {
                        val fact = factResponse.body()?.fact ?: ""
                        val image = imageResponse[0].url

                        _catsInfo.postValue(Result.Success(CatInfo(fact, image)))
                    } else {
                        CrashMonitor.trackWarning()
                    }
                } catch (e: SocketTimeoutException) {
                    _catsInfo.postValue(Result.Error("Не удалось получить ответ от сервером"))
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    CrashMonitor.trackWarning()
                    _catsInfo.postValue(Result.Error(e.message ?: "Произошла ошибка"))
                }
            }
        }
    }