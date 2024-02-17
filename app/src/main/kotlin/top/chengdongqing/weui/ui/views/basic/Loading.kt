package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun LoadingPage() {
    WePage(title = "Loading", description = "加载中") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            WeLoading()
            Spacer(Modifier.width(24.dp))
            WeLoading(size = 32.dp)
            Spacer(Modifier.width(24.dp))
            WeLoading(color = PrimaryColor)
            Spacer(Modifier.width(24.dp))
            WeLoading(size = 32.dp, color = PrimaryColor)
        }
    }
}