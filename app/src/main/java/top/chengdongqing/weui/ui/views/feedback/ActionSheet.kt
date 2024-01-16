package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.ActionSheetItem
import top.chengdongqing.weui.ui.components.feedback.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun ActionSheetPage() {
    Page(title = "ActionSheet", description = "弹出式菜单") {
        val actionSheet = rememberWeActionSheet()
        val toast = rememberWeToast()

        Column {
            WeButton(text = "弹出") {
                actionSheet.open(
                    "这是一个标题，可以为一行或者两行。",
                    listOf(
                        ActionSheetItem("微信"),
                        ActionSheetItem("支付宝", "副标题"),
                        ActionSheetItem("QQ钱包", "红色", color = Color.Red),
                        ActionSheetItem("小米钱包", "禁用", disabled = true),
                    )
                ) {
                    toast.open("点击了第${it + 1}个")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            WeButton(text = "通话", type = ButtonType.PLAIN) {
                val options = listOf(
                    ActionSheetItem("视频通话", icon = {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }),
                    ActionSheetItem("语音通话", icon = {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    })
                )

                actionSheet.open(
                    options = options
                ) {
                    toast.open("开始${options[it].label}")
                }
            }
        }
    }
}