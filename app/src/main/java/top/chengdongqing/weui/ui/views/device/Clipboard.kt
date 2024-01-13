package top.chengdongqing.weui.ui.views.device

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import top.chengdongqing.weui.ui.components.feedback.WeDialogHolder
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeTextarea

@Composable
fun ClipboardPage() {
    Page(title = "Clipboard", description = "剪贴板") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            var content by remember {
                mutableStateOf("")
            }

            WeTextarea(content, placeholder = "请输入内容", max = 200) {
                content = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeDialogHolder(title = "设置剪贴板失败", content = "内容不能为空") {
                WeButton(text = "设置剪贴板内容") {
                    if (content.isEmpty()) {
                        it.value = true
                    } else {
                        setClipboardData(context, content)
                        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            var content1 by remember {
                mutableStateOf("")
            }
            WeDialogHolder(title = "剪贴板内容", content = content1) { dialog ->
                WeButton(text = "读取剪贴板内容", type = ButtonType.PLAIN) {
                    getClipboardData(context)?.also {
                        content1 = it
                        dialog.value = true
                    } ?: Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show()
                }
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