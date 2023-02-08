package dallaz.winline.ui.workouts.adapter

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import dallaz.winline.R
import dallaz.winline.common.adapter.CommonViewHolder
import dallaz.winline.databinding.WorkoutItemBinding
import dallaz.winline.domain.models.Workout

class WorkoutViewHolder(
    binding: WorkoutItemBinding, private val onDelete: (Workout) -> Unit
) : CommonViewHolder<Workout, WorkoutItemBinding>(binding) {
    override fun bindItem(model: Workout) {
        binding.textView.text = model.type
        binding.durationView.isVisible = !model.isRunning
        if (!model.isRunning) {
            val diff: Long = model.endDate!!.time - model.startDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            binding.durationView.text =
                binding.root.context.getString(R.string.min, minutes)
        }
        binding.dateView.text = binding.root.context.getString(
            R.string.start_date,
            "${model.startDate.day}.${model.startDate.month}",
            "${model.startDate.hours}:${model.startDate.minutes}",
        )
        binding.deleteBtn.isInvisible = model.isRunning
        binding.deleteBtn.setOnClickListener {
            onDelete(model)
        }
    }
}