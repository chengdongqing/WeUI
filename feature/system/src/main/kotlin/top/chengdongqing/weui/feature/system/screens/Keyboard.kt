package top.chengdongqing.weui.feature.system.screens

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.format

@Composable
fun KeyboardScreen() {
    WeScreen(title = "Keyboard", description = "键盘") {
        val keyboardController = LocalSoftwareKeyboardController.current
        val keyboardHeight = rememberKeyboardHeight()

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        WeInput(
            value = null,
            placeholder = "键盘高度: ${keyboardHeight.value.format()}(dp)",
            modifier = Modifier.focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "弹出键盘") {
            keyboardController?.show()
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "收起键盘", type = ButtonType.PLAIN) {
            keyboardController?.hide()
        }
    }
}

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