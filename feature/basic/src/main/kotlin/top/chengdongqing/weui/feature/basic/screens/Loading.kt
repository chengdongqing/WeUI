package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.loading.DotDanceLoading
import top.chengdongqing.weui.core.ui.components.loading.MiLoadingMobile
import top.chengdongqing.weui.core.ui.components.loading.MiLoadingWeb
import top.chengdongqing.weui.core.ui.components.loading.WeLoading
import top.chengdongqing.weui.core.ui.components.loading.WeLoadingMP
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun LoadingScreen() {
    WeScreen(
        title = "Loading",
        description = "加载中",
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        WeLoading()
        WeLoading(size = 32.dp, color = MaterialTheme.colorScheme.primary)
        MiLoadingMobile()
        WeLoadingMP()
        DotDanceLoading()
        MiLoadingWeb()
    }
}