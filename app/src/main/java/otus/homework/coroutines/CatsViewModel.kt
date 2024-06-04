package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatsViewModel: ViewModel() {

    private val _liveData = MutableLiveData<List<Content>>()
    val liveData: LiveData<List<Content>> = _liveData


}