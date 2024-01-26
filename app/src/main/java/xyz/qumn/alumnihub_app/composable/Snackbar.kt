package xyz.qumn.alumnihub_app.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import xyz.qumn.alumnihub_app.AppState


data class SnackbarHelper(val isShow: MutableState<Boolean>) {
    fun show() {
        isShow.value = true
    }
}

@Composable
fun useSnackbar(msg: String): SnackbarHelper {
    val snackbarHostState = AppState.LocalSnackHostState.current
    val isShow = remember { mutableStateOf(false) }

    if (isShow.value) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(msg)
        }
    }
    return SnackbarHelper(isShow)
}
