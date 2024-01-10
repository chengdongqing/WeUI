package top.chengdongqing.weui.ui.views.feedback

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.ActionSheetItem
import top.chengdongqing.weui.ui.components.feedback.WeActionSheet
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun ActionSheetPage() {
    Page(title = "ActionSheet", description = "弹出式菜单") {
        val visible = remember {
            mutableStateOf(false)
        }

        WeButton(text = "弹出", type = ButtonType.PLAIN) {
            visible.value = true
        }

        val context = LocalContext.current
        WeActionSheet(
            visible.value,
            title = "这是一个标题，可以为一行或者两行。",
            options = listOf(
                ActionSheetItem("微信"),
                ActionSheetItem("支付宝", "副标题"),
                ActionSheetItem("QQ", color = Color.Red)
            ),
            onCancel = { visible.value = false }
        ) {
            Toast.makeText(context, "点击了第${it + 1}个", Toast.LENGTH_SHORT).show()
        }
    }
}