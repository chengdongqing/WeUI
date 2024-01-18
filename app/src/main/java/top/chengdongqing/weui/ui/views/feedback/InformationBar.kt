package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.InformationBarType
import top.chengdongqing.weui.ui.components.feedback.WeInformationBar
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun InformationBarPage() {
    Page(title = "Information Bar", description = "信息提示条") {
        Column(horizontalAlignment = Alignment.End) {
            val visible = remember {
                mutableStateOf(true)
            }
            WeInformationBar(
                visible = visible.value,
                content = "成功提示 success",
                type = InformationBarType.SUCCESS,
                linkText = "详情"
            ) {
                visible.value = false
            }
            if (!visible.value) {
                WeButton(text = "显示", type = ButtonType.PLAIN, size = ButtonSize.SMALL) {
                    visible.value = true
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar(
                content = "信息提示 warn strong",
                type = InformationBarType.WARN_STRONG
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar(content = "信息提示 warn weak", type = InformationBarType.WARN_WEAK)
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar(content = "信息提示 info", type = InformationBarType.INFO)
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar(
                content = "信息提示 tips strong",
                type = InformationBarType.TIPS_STRONG
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar(content = "信息提示 tips weak", type = InformationBarType.TIPS_WEAK)
        }
    }
}