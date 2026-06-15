package top.chengdongqing.weui.feature.feedback.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.ToastIcon
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.rememberToastState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ToastScreen(onBack: () -> Unit) {
    val toast = rememberToastState()
    val coroutineScope = rememberCoroutineScope()

    WeScreen(
        title = "Toast",
        description = "弹出式提示",
        verticalArrangement = Arrangement.spacedBy(20.dp),
        onBack = onBack
    ) {
        WeButton(text = "成功提示", type = ButtonType.Plain) {
            toast.show(title = "已完成", icon = ToastIcon.Success)
        }
        WeButton(text = "失败提示", type = ButtonType.Plain) {
            toast.show(title = "获取链接失败", icon = ToastIcon.Fail)
        }
        WeButton(text = "长文案提示", type = ButtonType.Plain) {
            toast.show(title = "此处为长文案提示详情", icon = ToastIcon.Fail)
        }
        WeButton(text = "立即支付", type = ButtonType.Plain) {
            toast.show(
                title = "支付中...",
                icon = ToastIcon.Loading,
                duration = Duration.INFINITE,
                mask = true
            )
            coroutineScope.launch {
                delay(2000.milliseconds)
                toast.hide()
                delay(200.milliseconds)
                toast.show(title = "支付成功", icon = ToastIcon.Success)
            }
        }
        WeButton(text = "文字提示", type = ButtonType.Plain) {
            toast.show("文字提示")
        }
    }
}