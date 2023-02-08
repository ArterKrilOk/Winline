package dallaz.winline.database.db

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun dateToLong(date: Date?): Long {
        return date?.time ?: -1L
    }

    @TypeConverter
    fun longToDate(time: Long): Date? {
        return if (time == -1L) null
        else Date(time)
    }
}