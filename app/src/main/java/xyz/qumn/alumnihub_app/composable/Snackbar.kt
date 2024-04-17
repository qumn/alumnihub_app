package xyz.qumn.alumnihub_app.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import xyz.qumn.alumnihub_app.AppState


data class SnackbarHelper(val _msg: MutableState<String?>) {
    fun show(msg: String) {
        _msg.value = msg
    }
}

@Composable
fun useSnack(): SnackbarHelper {
    val snackHostState = AppState.LocalSnackHostState.current
    val msg: MutableState<String?> = remember { mutableStateOf(null) }

    if (msg.value != null) {
        LaunchedEffect(Unit) {
            snackHostState?.showSnackbar(msg.value!!)
            msg.value = null
        }
    }
    return SnackbarHelper(msg)
}
