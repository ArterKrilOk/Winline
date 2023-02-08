package dallaz.winline.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import dallaz.winline.database.entities.WorkoutRoomEntity

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workoutRoomEntity: WorkoutRoomEntity): Long

    @Update
    suspend fun update(workoutRoomEntity: WorkoutRoomEntity)

    @Delete
    suspend fun delete(workoutRoomEntity: WorkoutRoomEntity)

    @Query("SELECT * FROM workouts ORDER BY startDate DESC")
    fun getWorkoutsPaged(): PagingSource<Int, WorkoutRoomEntity>

    @Query("SELECT * FROM workouts WHERE id=:id")
    suspend fun getWorkout(id: Long): WorkoutRoomEntity
}