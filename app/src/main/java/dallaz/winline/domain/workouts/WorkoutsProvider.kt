package dallaz.winline.domain.workouts

import dallaz.winline.app.prefs.AppPrefs
import dallaz.winline.database.dao.WorkoutDao
import dallaz.winline.database.entities.WorkoutRoomEntity
import dallaz.winline.domain.exceptions.NoWorkoutException
import dallaz.winline.domain.models.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class WorkoutsProvider @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val appPrefs: AppPrefs
) {
    private val currentWorkoutRoomEntity = appPrefs.currentWorkoutFlow.map {
        if (it == AppPrefs.NO_CURRENT_WORKOUT_VAL) null
        else workoutDao.getWorkout(it)
    }.flowOn(Dispatchers.IO)

    val currentWorkout = currentWorkoutRoomEntity.map {
        it?.let {
            Workout(it.id, it.type, it.startDate, it.endDate)
        }
    }

    suspend fun stopWorkout() = withContext(Dispatchers.IO) {
        val id = appPrefs.currentWorkout
        if (id == AppPrefs.NO_CURRENT_WORKOUT_VAL) throw NoWorkoutException()
        val workout = workoutDao.getWorkout(id)
        workoutDao.update(workout.copy(endDate = Date()))
        appPrefs.currentWorkout = AppPrefs.NO_CURRENT_WORKOUT_VAL
    }

    suspend fun startWorkout(type: String) = withContext(Dispatchers.IO) {
        val workoutId = workoutDao.insert(
            WorkoutRoomEntity(type = type, startDate = Date())
        )
        appPrefs.currentWorkout = workoutId
    }

    suspend fun removeWorkout(id: Long) = withContext(Dispatchers.IO) {
        if (id == appPrefs.currentWorkout) {
            appPrefs.currentWorkout = AppPrefs.NO_CURRENT_WORKOUT_VAL
        }
        val workout = workoutDao.getWorkout(id)
        workoutDao.delete(workout)
    }

    fun getWorkoutsPaged() = workoutDao.getWorkoutsPaged()
}