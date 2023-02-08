package dallaz.winline.ui.workouts

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dallaz.winline.app.App
import dallaz.winline.common.mvvm.CommonViewModel
import dallaz.winline.domain.models.Workout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class WorkoutsViewModel(app: App, vararg args: Any) : CommonViewModel(app) {
    private val workoutsProvider = appComponent.workoutsProvider
    val state = workoutsProvider.currentWorkout.map {
        if (it == null) State.CREATE
        else State.CONTROL
    }.shareWhileSubscribed()

    val newWorkoutType = MutableStateFlow("")

    val canCreate = newWorkoutType.map {
        it.isNotEmpty()
    }.shareWhileSubscribed()

    val workoutsPaging = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        workoutsProvider.getWorkoutsPaged()
    }.flow.cachedIn(viewModelScope).map { paging ->
        paging.map {
            Workout(it.id, it.type, it.startDate, it.endDate)
        }
    }.shareWhileSubscribed()

    private val currentWorkout = workoutsProvider.currentWorkout.filterNotNull()
        .shareWhileSubscribed()

    private val tickerFlow = flow {
        while (true) {
            delay(10)
            emit(Unit)
        }
    }.shareWhileSubscribed()

    val timerFlow = combine(
        currentWorkout,
        tickerFlow,
    ) { workout, _ ->
        val diff = Date().time - workout.startDate.time
        val millis = (diff % 1000) / 10
        val sec = (diff / 1000) % 60
        val min = diff / 1000 / 60
        Timer(millis, sec, min)
    }.shareWhileSubscribed()

    fun removeWorkout(id: Long) = viewModelScope.launch {
        workoutsProvider.removeWorkout(id)
    }

    fun removeActiveWorkout() = viewModelScope.launch {
        workoutsProvider.stopWorkout()
    }

    fun removeAndDeleteActiveWorkout() = viewModelScope.launch {
        currentWorkout.take(1).collect {
            workoutsProvider.removeWorkout(it.id)
        }
    }

    fun createWorkout() = viewModelScope.launch {
        newWorkoutType.take(1).collect {
            workoutsProvider.startWorkout(it)
        }
    }
}

enum class State {
    CREATE,
    CONTROL
}

data class Timer(
    val millis: Long,
    val sec: Long,
    val min: Long
)