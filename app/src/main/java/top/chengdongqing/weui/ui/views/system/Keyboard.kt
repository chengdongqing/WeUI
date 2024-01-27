package top.chengdongqing.weui.ui.views.system

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeInput
import top.chengdongqing.weui.utils.formatFloat

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyboardPage() {
    WePage(title = "Keyboard", description = "键盘") {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember {
            FocusRequester()
        }
        val keyboardHeight = rememberKeyboardHeight()

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeInput(
                value = "",
                placeholder = "键盘高度: ${formatFloat(keyboardHeight.value)}(dp)",
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberKeyboardHeight(): Dp {
    val rootView = LocalView.current
    val density = LocalDensity.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var height by remember {
        mutableStateOf(0.dp)
    }

    DisposableEffect(keyboardController) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val windowInsets = ViewCompat.getRootWindowInsets(rootView)
            val bottomInset =
                windowInsets?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: 0
            val keyboardHeight = rootView.height - rect.bottom - bottomInset
            height = density.run {
                keyboardHeight.toDp()
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return height
}
