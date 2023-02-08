package dallaz.winline.common.prefs

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking

open class FlowPrefs(protected val prefs: SharedPreferences) {
    private val flows = mutableMapOf<String, Flow<*>>()

    protected fun <T> getPropertyFlow(
        propertyName: String,
        getProperty: (SharedPreferences, String) -> T
    ): Flow<T> = flows.getOrPut(propertyName) {
        var isComplete = false
        callbackFlow {
            val callback =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, name ->
                    if (!isComplete)
                        runBlocking { send(getProperty(sharedPreferences, name)) }
                }
            prefs.registerOnSharedPreferenceChangeListener(callback)
            send(getProperty(prefs, propertyName))
            awaitClose {
                isComplete = true
                prefs.unregisterOnSharedPreferenceChangeListener(callback)
                flows.remove(propertyName)
            }
        }
    } as Flow<T>
}