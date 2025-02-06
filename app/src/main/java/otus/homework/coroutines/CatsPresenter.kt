package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                // Переходим к фоновому потоку для выполнения запроса
                val fact = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                // Добавляем факт
                _catsView?.populate(fact)
            } catch (e: Exception) {
                // Обрабатываем ошибку, если необходимо
                CrashMonitor.trackWarning()

            }
        }
//        catsService.getCatFact()(object : Callback<Fact> {
//
//            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
//                if (response.isSuccessful && response.body() != null) {
//                    _catsView?.populate(response.body()!!)
//                }
//            }
//
//            override fun onFailure(call: Call<Fact>, t: Throwable) {
//                CrashMonitor.trackWarning()
//            }
//        })
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.clear()
    }
}