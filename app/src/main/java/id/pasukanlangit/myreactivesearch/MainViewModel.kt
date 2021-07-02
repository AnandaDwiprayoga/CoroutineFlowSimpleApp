package id.pasukanlangit.myreactivesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.pasukanlangit.myreactivesearch.network.ApiConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel
        .asFlow()
        .debounce(3000)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotBlank()
        }
        .mapLatest {
            ApiConfig.provideApiService().getCountry(it, accessToken = BuildConfig.API_KEY).features
        }
        .asLiveData()


}