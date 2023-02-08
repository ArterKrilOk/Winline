package dallaz.winline.app.prefs

import android.content.SharedPreferences
import dallaz.winline.app.di.AppScope
import dallaz.winline.common.prefs.FlowPrefs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

@AppScope
class AppPrefs @Inject constructor(
    @Named("app_prefs") prefs: SharedPreferences
) : FlowPrefs(prefs) {

    var url: String
        get() = prefs.getString(URL_NAME, URL_DEFAULT_VAL) ?: URL_DEFAULT_VAL
        set(value) = prefs.edit().putString(URL_NAME, value).apply()

    val urlFlow: Flow<String> = getPropertyFlow(URL_NAME) { prefs, name ->
        prefs.getString(name, URL_DEFAULT_VAL) ?: URL_DEFAULT_VAL
    }

    var currentWorkout: Long
        get() = prefs.getLong(CURRENT_WORKOUT, NO_CURRENT_WORKOUT_VAL)
        set(value) = prefs.edit().putLong(CURRENT_WORKOUT, value).apply()
    val currentWorkoutFlow: Flow<Long> = getPropertyFlow(CURRENT_WORKOUT) { prefs, name ->
        prefs.getLong(name, NO_CURRENT_WORKOUT_VAL)
    }

    companion object {
        private const val URL_NAME = "url"
        private const val URL_DEFAULT_VAL = ""

        private const val CURRENT_WORKOUT = "workout"
        const val NO_CURRENT_WORKOUT_VAL = -1L
    }
}