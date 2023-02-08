package dallaz.winline.ui.main

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import dallaz.winline.R
import dallaz.winline.common.mvvm.viewModels
import dallaz.winline.databinding.ActivityMainBinding
import dallaz.winline.domain.exceptions.DeviceConfigurationException
import dallaz.winline.domain.exceptions.FRCException
import dallaz.winline.domain.exceptions.NoInternetConnectionException
import dallaz.winline.ui.UiUtils.repeatOnStarted
import dallaz.winline.ui.web.WebActivity
import dallaz.winline.ui.workouts.WorkoutsActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
    }

    private fun setObservers() {
        repeatOnStarted {
            viewModel.loading.collect {
                binding.progressCircular.isVisible = it
            }
        }
        repeatOnStarted {
            viewModel.singleUrl.collect {
                if (it.isEmpty()) WorkoutsActivity.startActivity(this@MainActivity)
                else WebActivity.startWebActivity(this@MainActivity, it)
            }
        }
        repeatOnStarted {
            viewModel.exception.collect {
                binding.errorView.isVisible = true
                when (it) {
                    is NoInternetConnectionException -> binding.errorView.setIconAndText(
                        R.drawable.no_internet_connection_24,
                        R.string.no_internet
                    )
                    is DeviceConfigurationException -> binding.errorView.setIconAndText(
                        R.drawable.wrong_device_24,
                        R.string.wrong_device
                    )
                    is FRCException -> binding.errorView.setIconAndText(
                        R.drawable.error_outline_24,
                        R.string.config_sync_error
                    )
                    else -> binding.errorView.setIconAndText(
                        R.drawable.error_outline_24,
                        R.string.something_went_wrong
                    )
                }
            }
        }
    }

    private fun AppCompatTextView.setIconAndText(
        @DrawableRes drawableRes: Int,
        @StringRes stringRes: Int
    ) {
        setText(stringRes)
        setCompoundDrawablesWithIntrinsicBounds(
            drawableRes, 0, 0, 0,
        )
    }
}