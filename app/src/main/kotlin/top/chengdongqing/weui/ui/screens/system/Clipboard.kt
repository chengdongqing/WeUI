package top.chengdongqing.weui.ui.screens.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.dialog.rememberDialogState
import top.chengdongqing.weui.ui.components.input.WeTextarea
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState

@Composable
fun ClipboardScreen() {
    WeScreen(title = "Clipboard", description = "剪贴板") {
        var content by remember { mutableStateOf("") }
        val context = LocalContext.current
        val dialog = rememberDialogState()
        val toast = rememberToastState()

        WeTextarea(content, placeholder = "请输入内容", max = 200, topBorder = true) {
            content = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "设置剪贴板内容") {
            if (content.isEmpty()) {
                toast.show("内容不能为空", ToastIcon.FAIL)
            } else {
                setClipboardData(context, content)
                toast.show("已复制", ToastIcon.SUCCESS)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "读取剪贴板内容", type = ButtonType.PLAIN) {
            getClipboardData(context)?.let {
                dialog.show(
                    title = "剪贴板内容",
                    content = it,
                    onCancel = null
                )
            } ?: toast.show("获取失败", ToastIcon.FAIL)
        }
    }
}

fun getClipboardData(context: Context): String? {
    return (context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.run {
        val clip = primaryClip
        if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).text.toString()
        } else {
            null
        }
    }
}

fun setClipboardData(context: Context, content: String) {
    (context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.apply {
        val clip = ClipData.newPlainText("label", content)
        setPrimaryClip(clip)
    }
}