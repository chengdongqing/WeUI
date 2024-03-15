package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.loading.DotDanceLoading
import top.chengdongqing.weui.ui.components.loading.MiLoadingMobile
import top.chengdongqing.weui.ui.components.loading.MiLoadingWeb
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.components.loading.WeLoadingMP
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun LoadingScreen() {
    WeScreen(
        title = "Loading",
        description = "加载中",
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            WeLoading(size = 32.dp)
            Spacer(Modifier.width(24.dp))
            WeLoading()
            Spacer(Modifier.width(24.dp))
            WeLoading(color = PrimaryColor)
            Spacer(Modifier.width(24.dp))
            WeLoading(size = 32.dp, color = PrimaryColor)
        }
        WeLoadingMP()
        Row {
            MiLoadingMobile()
            Spacer(Modifier.width(24.dp))
            MiLoadingMobile(PrimaryColor)
        }
        DotDanceLoading()
        MiLoadingWeb()
    }
}