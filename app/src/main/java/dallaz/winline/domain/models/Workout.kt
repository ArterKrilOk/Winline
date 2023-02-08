package dallaz.winline.domain.models

import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class Workout(
    val id: Long,
    val type: String,
    val startDate: Date,
    val endDate: Date?
) {
    val isRunning = endDate == null

    companion object {
        val DIFF_UTILS = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Workout, newItem: Workout) =
                oldItem == newItem
        }
    }
}
