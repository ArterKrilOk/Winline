package dallaz.winline.common.mvvm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import dallaz.winline.app.App

class CommonFactory(private val app: App, vararg args: Any) : ViewModelProvider.Factory {
    private val varargs = args

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(App::class.java, Array<out Any>::class.java)
            .newInstance(app, varargs)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return modelClass
            .getConstructor(App::class.java, Array<out Any>::class.java)
            .newInstance(app, varargs)
    }
}