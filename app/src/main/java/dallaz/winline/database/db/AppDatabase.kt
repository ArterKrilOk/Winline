package dallaz.winline.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dallaz.winline.database.dao.WorkoutDao
import dallaz.winline.database.entities.WorkoutRoomEntity

@Database(
    entities = [
        WorkoutRoomEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutsDao(): WorkoutDao
}