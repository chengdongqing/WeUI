package top.chengdongqing.weui.ui.views.device

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.WeDialogHolder
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun ClipboardPage() {
    Page(title = "Clipboard", description = "剪贴板") {
        val context = LocalContext.current

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val content = remember {
                mutableStateOf("")
            }
            TextField(value = content.value, onValueChange = {
                content.value = it
            })
            Spacer(modifier = Modifier.height(20.dp))
            WeDialogHolder(title = "设置剪贴板失败", content = "内容不能为空") {
                WeButton(text = "设置剪贴板内容") {
                    if (content.value.isEmpty()) {
                        it.value = true
                    } else {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("label", content.value)
                        clipboard.setPrimaryClip(clip)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            val content1 = remember {
                mutableStateOf("")
            }
            WeDialogHolder(title = "剪贴板内容", content = content1.value) {
                WeButton(text = "读取剪贴板内容", type = ButtonType.PLAIN) {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = clipboard.primaryClip
                    if (clip != null && clip.itemCount > 0) {
                        content1.value = clip.getItemAt(0).text.toString()
                        println("content1: ${content1.value}")
                        it.value = true
                    } else {
                        Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}