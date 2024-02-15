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
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.informationbar.InformationBarType
import top.chengdongqing.weui.ui.components.informationbar.WeInformationBar
import top.chengdongqing.weui.ui.components.page.WePage

@Composable
fun InformationBarPage() {
    WePage(title = "InformationBar", description = "信息提示条") {
        Column(horizontalAlignment = Alignment.End) {
            val (visible, setVisible) = remember { mutableStateOf(true) }
            WeInformationBar(
                visible = visible,
                content = "成功提示 success",
                type = InformationBarType.SUCCESS,
                linkText = "详情"
            ) {
                setVisible(false)
            }
            if (!visible) {
                WeButton(text = "显示", type = ButtonType.PLAIN, size = ButtonSize.SMALL) {
                    setVisible(true)
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