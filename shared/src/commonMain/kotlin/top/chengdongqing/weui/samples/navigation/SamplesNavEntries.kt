package top.chengdongqing.weui.samples.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.navigation.SamplesNavKey
import top.chengdongqing.weui.samples.ui.NotificationBarScreen
import top.chengdongqing.weui.samples.ui.PanoramicImageScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.samplesNavEntries(onBack: () -> Unit) {
    entry<SamplesNavKey.NotificationBar>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        NotificationBarScreen(onBack)
    }
    entry<SamplesNavKey.PanoramicImage>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        PanoramicImageScreen(onBack)
    }
}