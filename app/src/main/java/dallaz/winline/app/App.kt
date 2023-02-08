package dallaz.winline.app

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dallaz.winline.app.di.AppComponent
import dallaz.winline.app.di.AppModule
import dallaz.winline.app.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().build())
        }
    }
}