package dallaz.winline.ui.workouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import dallaz.winline.R
import dallaz.winline.common.mvvm.viewModels
import dallaz.winline.databinding.ActivityWorkoutsBinding
import dallaz.winline.ui.UiUtils.onTextChanged
import dallaz.winline.ui.UiUtils.repeatOnStarted
import dallaz.winline.ui.workouts.adapter.WorkoutViewAdapter
import kotlinx.coroutines.flow.collectLatest

class WorkoutsActivity : AppCompatActivity() {
    private val workoutsAdapter by lazy {
        WorkoutViewAdapter {
            viewModel.removeWorkout(it.id)
        }
    }
    private val viewModel by viewModels<WorkoutsViewModel>()
    private lateinit var binding: ActivityWorkoutsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = workoutsAdapter
            layoutManager = LinearLayoutManager(this@WorkoutsActivity)
        }
        binding.autoCompleteTextView.apply {
            setAdapter(
                ArrayAdapter(
                    this@WorkoutsActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    resources.getStringArray(R.array.workout_names)
                )
            )
            threshold = 0
        }

        setObservers()
    }

    private fun setObservers() {
        repeatOnStarted {
            binding.autoCompleteTextView.onTextChanged().collect(viewModel.newWorkoutType)
        }
        repeatOnStarted {
            viewModel.canCreate.collect {
                binding.createBtn.isEnabled = it
            }
        }
        repeatOnStarted {
            viewModel.state.collect {
                binding.createLayout.isVisible = it == State.CREATE
                binding.workoutControlsLay.isVisible = it == State.CONTROL
                binding.timerView.isVisible = it == State.CONTROL
            }
        }
        repeatOnStarted {
            viewModel.workoutsPaging.collectLatest {
                workoutsAdapter.submitData(it)
            }
        }
        repeatOnStarted {
            viewModel.timerFlow.collect {
                binding.timerView.text = getString(
                    R.string.timer,
                    it.min,
                    it.sec,
                    it.millis
                )
            }
        }

        binding.createBtn.setOnClickListener {
            viewModel.createWorkout()
        }

        binding.finishBtn.setOnClickListener {
            viewModel.removeActiveWorkout()
        }

        binding.removeBtn.setOnClickListener {
            viewModel.removeAndDeleteActiveWorkout()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, WorkoutsActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }
}