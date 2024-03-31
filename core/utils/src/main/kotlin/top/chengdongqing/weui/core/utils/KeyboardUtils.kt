package top.chengdongqing.weui.core.utils

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun rememberKeyboardHeight(): Dp {
    val view = LocalView.current
    val density = LocalDensity.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var height by remember { mutableStateOf(0.dp) }

    DisposableEffect(keyboardController) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val windowInsets = ViewCompat.getRootWindowInsets(view)
            val bottomInset =
                windowInsets?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: 0
            val keyboardHeight = view.height - rect.bottom - bottomInset
            height = density.run {
                keyboardHeight.toDp()
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return height
}