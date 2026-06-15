package top.chengdongqing.weui.feature.system.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.ToastIcon
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.input.WeTextarea
import top.chengdongqing.weui.core.ui.components.rememberDialogState
import top.chengdongqing.weui.core.ui.components.rememberToastState
import top.chengdongqing.weui.util.getClipboard

@Composable
fun ClipboardScreen(onBack: () -> Unit) {
    var data by remember { mutableStateOf("") }
    val toast = rememberToastState()
    val dialog = rememberDialogState()
    val scope = rememberCoroutineScope()

    WeScreen(
        title = "Clipboard",
        description = "剪贴板",
        onBack = onBack
    ) {
        WeTextarea(
            value = data,
            max = 200,
            placeholder = "请输入内容",
            topBorder = true
        ) {
            data = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "设置剪贴板内容") {
            if (data.isEmpty()) {
                toast.show("内容不能为空", ToastIcon.Fail)
            } else {
                scope.launch {
                    getClipboard().setClipboardData(data)
                    toast.show("已复制", ToastIcon.Success)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "读取剪贴板内容", type = ButtonType.Plain) {
            scope.launch {
                getClipboard().getClipboardData()?.let {
                    dialog.show(
                        title = "剪贴板内容",
                        content = it,
                        onCancel = null
                    )
                } ?: toast.show("获取失败", ToastIcon.Fail)
            }
        }
    }
}