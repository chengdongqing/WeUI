package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.InformationBarType
import top.chengdongqing.weui.ui.components.feedback.WeInformationBar

@Composable
fun InformationBarPage() {
    Page(title = "Information Bar", description = "信息提示条") {
        Column {
            WeInformationBar("成功提示 success", InformationBarType.SUCCESS, linkText = "详情") {}
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar("信息提示 warn strong", InformationBarType.WARN_STRONG)
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar("信息提示 warn weak", InformationBarType.WARN_WEAK)
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar("信息提示 info", InformationBarType.INFO)
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar("信息提示 tips strong", InformationBarType.TIPS_STRONG)
            Spacer(modifier = Modifier.height(20.dp))
            WeInformationBar("信息提示 tips weak", InformationBarType.TIPS_WEAK)
        }
    }
}