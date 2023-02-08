package dallaz.winline.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "workouts")
data class WorkoutRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val type: String,
    @ColumnInfo
    val startDate: Date,
    @ColumnInfo
    var endDate: Date? = null
)
