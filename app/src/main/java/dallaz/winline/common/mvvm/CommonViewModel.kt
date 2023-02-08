package dallaz.winline.common.mvvm

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dallaz.winline.app.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

open class CommonViewModel(app: App, vararg args: Any) : AndroidViewModel(app) {
    val appComponent by lazy { app.appComponent }

    protected fun <T> Flow<T>.shareWhileSubscribed(replay: Int = 1, expire: Long = Long.MAX_VALUE) =
        this.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(replayExpirationMillis = expire),
            replay
        )
}