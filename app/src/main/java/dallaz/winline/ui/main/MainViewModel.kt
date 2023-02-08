package dallaz.winline.ui.main

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import dallaz.winline.BuildConfig
import dallaz.winline.app.App
import dallaz.winline.common.mvvm.CommonViewModel
import kotlinx.coroutines.flow.*
import java.util.*


class MainViewModel(private val app: App, vararg args: Any) : CommonViewModel(app) {
    val exception = MutableSharedFlow<Throwable>()

    val loading = MutableStateFlow(true)

    val singleUrl = appComponent.urlProvider.urlFlow.take(1).onStart {
        loading.emit(true)
    }.map {
        if (checkIsEmu() || !isSimAvailable()) ""
        else it
    }.catch {
        exception.emit(it)
    }.onCompletion {
        loading.emit(false)
    }.shareWhileSubscribed()


    private fun isSimAvailable(): Boolean {
        val manager = app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.simState == TelephonyManager.SIM_STATE_READY
    }

    private fun checkIsEmu(): Boolean {
        if (BuildConfig.DEBUG) return false // when developer use this build on emulator
        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        var result = (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
        if (result) return true
        result = result or (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        if (result) return true
        return result or ("google_sdk" == buildProduct)
    }
}