package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.WeToastHolder
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ToastPage() {
    Page(title = "Toast", description = "弹出式提示") {
        Column {
            WeToastHolder(title = "已完成", icon = ToastIcon.SUCCESS) {
                WeButton(text = "成功提示", type = ButtonType.PLAIN) {
                    it.value = true
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeToastHolder(title = "获取链接失败", icon = ToastIcon.FAIL) {
                WeButton(text = "失败提示", type = ButtonType.PLAIN) {
                    it.value = true
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeToastHolder(title = "此处为长文案提示详情", icon = ToastIcon.FAIL) {
                WeButton(text = "长文案提示", type = ButtonType.PLAIN) {
                    it.value = true
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            val coroutineScope = rememberCoroutineScope()
            WeToastHolder(title = "支付成功", icon = ToastIcon.SUCCESS) { finish ->
                WeToastHolder(
                    title = "支付中...",
                    icon = ToastIcon.LOADING,
                    mask = true,
                    duration = Duration.INFINITE
                ) {
                    WeButton(text = "立即支付", type = ButtonType.PLAIN) {
                        it.value = true

                        coroutineScope.launch {
                            delay(2000.milliseconds)
                            it.value = false
                            finish.value = true
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeToastHolder(title = "文字提示") {
                WeButton(text = "文字提示", type = ButtonType.PLAIN) {
                    it.value = true
                }
            }
        }
    }
}