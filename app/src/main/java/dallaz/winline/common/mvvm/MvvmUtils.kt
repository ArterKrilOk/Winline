package dallaz.winline.common.mvvm

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import dallaz.winline.app.App

inline fun <reified VM : CommonViewModel> ComponentActivity.viewModels(
    vararg args: Any
): Lazy<VM> {
    val factoryProducer: () -> ViewModelProvider.Factory = {
        if (application !is App) throw IllegalStateException("Application is not App instance")
        CommonFactory(application as App, args)
    }

    return ViewModelLazy(VM::class, { viewModelStore }, factoryProducer)
}