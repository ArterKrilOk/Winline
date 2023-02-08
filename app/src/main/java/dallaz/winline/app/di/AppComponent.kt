package dallaz.winline.app.di

import dagger.Component
import dallaz.winline.database.di.DatabaseModule
import dallaz.winline.domain.url.UrlProvider
import dallaz.winline.domain.workouts.WorkoutsProvider

@AppScope
@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    val urlProvider: UrlProvider
    val workoutsProvider: WorkoutsProvider
}