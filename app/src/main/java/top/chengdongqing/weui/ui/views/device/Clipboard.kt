package top.chengdongqing.weui.ui.views.device

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.rememberWeDialog
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeTextarea

@Composable
fun ClipboardPage() {
    Page(title = "Clipboard", description = "剪贴板") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            val weDialog = rememberWeDialog()
            val toast = rememberWeToast()

            var content by remember {
                mutableStateOf("")
            }

            WeTextarea(content, placeholder = "请输入内容", max = 200, topBorder = true) {
                content = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "设置剪贴板内容") {
                if (content.isEmpty()) {
                    weDialog.open("设置剪贴板失败", "内容不能为空", onCancel = null)
                } else {
                    setClipboardData(context, content)
                    toast.open("已复制", ToastIcon.SUCCESS)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "读取剪贴板内容", type = ButtonType.PLAIN) {
                getClipboardData(context)?.let {
                    weDialog.open("剪贴板内容", it, onCancel = null)
                } ?: toast.open("获取失败", ToastIcon.FAIL)
            }
        }
    }
}

private fun getClipboardData(context: Context): String? {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboard.primaryClip
    if (clip != null && clip.itemCount > 0) {
        return clip.getItemAt(0).text.toString()
    }
    return null
}

private fun setClipboardData(context: Context, content: String) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", content)
    clipboard.setPrimaryClip(clip)
}