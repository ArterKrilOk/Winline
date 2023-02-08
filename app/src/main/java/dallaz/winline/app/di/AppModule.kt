package dallaz.winline.app.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideAppContext(): Context = context

    @Provides
    @AppScope
    @Named("app_prefs")
    fun provideAppPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences("app-prefs", Context.MODE_PRIVATE)
}