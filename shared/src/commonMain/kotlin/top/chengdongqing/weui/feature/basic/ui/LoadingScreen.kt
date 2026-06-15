package top.chengdongqing.weui.feature.basic.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.loading.DotDanceLoading
import top.chengdongqing.weui.core.ui.components.loading.MiLoadingMobile
import top.chengdongqing.weui.core.ui.components.loading.MiLoadingWeb
import top.chengdongqing.weui.core.ui.components.loading.WeLoading
import top.chengdongqing.weui.core.ui.components.loading.WeLoadingMP
import top.chengdongqing.weui.core.ui.theme.WeTheme

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