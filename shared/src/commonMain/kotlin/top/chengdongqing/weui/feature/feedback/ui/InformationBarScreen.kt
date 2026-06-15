package top.chengdongqing.weui.feature.feedback.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonSize
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.InformationBarType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeInformationBar
import top.chengdongqing.weui.core.ui.components.WeScreen

@Composable
fun InformationBarScreen(onBack: () -> Unit) {
    val (visible, setVisible) = remember { mutableStateOf(true) }

    WeScreen(
        title = "InformationBar",
        description = "信息提示条",
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        onBack = onBack
    ) {
        WeInformationBar(
            visible = visible,
            message = "成功提示 success",
            type = InformationBarType.Success,
            linkText = "详情"
        ) {
            setVisible(false)
        }
        if (!visible) {
            WeButton(
                text = "显示",
                type = ButtonType.Plain,
                size = ButtonSize.Small
            ) {
                setVisible(true)
            }
        }
        WeInformationBar(
            message = "信息提示 warn strong",
            type = InformationBarType.WarnStrong
        )
        WeInformationBar(
            message = "信息提示 info",
            type = InformationBarType.Info
        )
        WeInformationBar(
            message = "信息提示 tips strong",
            type = InformationBarType.TipsStrong
        )
        WeInformationBar(
            message = "信息提示 tips weak",
            type = InformationBarType.TipsWeak
        )
    }
}