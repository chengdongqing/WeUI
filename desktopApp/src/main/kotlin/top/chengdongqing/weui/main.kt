package top.chengdongqing.weui

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    val windowState = WindowState(
        size = DpSize(400.dp, 800.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "WeUI"
    ) {
        App()
    }
}