package top.chengdongqing.weui.feature.feedback.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.informationbar.InformationBarType
import top.chengdongqing.weui.core.ui.components.informationbar.WeInformationBar
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun InformationBarScreen() {
    WeScreen(
        title = "InformationBar",
        description = "信息提示条",
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
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
        WeInformationBar(
            content = "信息提示 warn strong",
            type = InformationBarType.WARN_STRONG
        )
        WeInformationBar(content = "信息提示 info", type = InformationBarType.INFO)
        WeInformationBar(
            content = "信息提示 tips strong",
            type = InformationBarType.TIPS_STRONG
        )
        WeInformationBar(content = "信息提示 tips weak", type = InformationBarType.TIPS_WEAK)
    }
}