package dallaz.winline.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.runBlocking

object UiUtils {
    fun Snackbar.top(): Snackbar = apply {
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
    }

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenStarted(block)
    }

    fun LifecycleOwner.repeatOnCreated(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenCreated(block)
    }

    fun LifecycleOwner.repeatOnResumed(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenResumed(block)
    }

    fun EditText.onTextChanged(debounce: Long = 200) = callbackFlow {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                runBlocking { send(p0.toString()) }
            }

            override fun afterTextChanged(p0: Editable?) {
                runBlocking { send(p0.toString()) }
            }
        }
        addTextChangedListener(listener)
        send(text.toString())
        awaitClose { removeTextChangedListener(listener) }
    }.debounce(debounce).distinctUntilChanged()
}