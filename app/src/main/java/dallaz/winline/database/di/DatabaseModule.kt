package dallaz.winline.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dallaz.winline.app.di.AppScope
import dallaz.winline.database.dao.WorkoutDao
import dallaz.winline.database.db.AppDatabase

@Module
class DatabaseModule {
    @AppScope
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "workoutsdb")
            .fallbackToDestructiveMigration()
            .build()

    @AppScope
    @Provides
    fun provideWorkoutsDao(database: AppDatabase): WorkoutDao =
        database.workoutsDao()
}