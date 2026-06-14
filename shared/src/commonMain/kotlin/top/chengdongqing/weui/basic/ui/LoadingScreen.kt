package top.chengdongqing.weui.basic.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.components.loading.DotDanceLoading
import top.chengdongqing.weui.components.loading.MiLoadingMobile
import top.chengdongqing.weui.components.loading.MiLoadingWeb
import top.chengdongqing.weui.components.loading.WeLoading
import top.chengdongqing.weui.components.loading.WeLoadingMP
import top.chengdongqing.weui.theme.WeTheme

@Composable
fun LoadingScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Loading",
        description = "加载中",
        verticalArrangement = Arrangement.spacedBy(40.dp),
        onBack = onBack
    ) {
        WeLoading()
        WeLoading(size = 32.dp, color = WeTheme.colorScheme.primary)
        MiLoadingMobile()
        WeLoadingMP()
        DotDanceLoading()
        MiLoadingWeb()
    }
}