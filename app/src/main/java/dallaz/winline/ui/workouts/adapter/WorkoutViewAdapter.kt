package dallaz.winline.ui.workouts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import dallaz.winline.common.adapter.CommonPagingAdapter
import dallaz.winline.databinding.WorkoutItemBinding
import dallaz.winline.domain.models.Workout

class WorkoutViewAdapter(
    private val onDelete: (Workout) -> Unit
) : CommonPagingAdapter<Workout, WorkoutViewHolder>(Workout.DIFF_UTILS) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkoutViewHolder(
        WorkoutItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onDelete
    )
}