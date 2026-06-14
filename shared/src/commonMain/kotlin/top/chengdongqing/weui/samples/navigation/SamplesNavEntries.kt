package top.chengdongqing.weui.samples.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.navigation.SamplesNavKey
import top.chengdongqing.weui.samples.ui.CubicBezierScreen
import top.chengdongqing.weui.samples.ui.DigitalKeyboardScreen
import top.chengdongqing.weui.samples.ui.DigitalRollerScreen
import top.chengdongqing.weui.samples.ui.DividingRuleScreen
import top.chengdongqing.weui.samples.ui.DropCardScreen
import top.chengdongqing.weui.samples.ui.IndexedListScreen
import top.chengdongqing.weui.samples.ui.NotificationBarScreen
import top.chengdongqing.weui.samples.ui.OrgTreeScreen
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
    entry<SamplesNavKey.CubicBezier>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        CubicBezierScreen(onBack)
    }
    entry<SamplesNavKey.DigitalKeyboard>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DigitalKeyboardScreen(onBack)
    }
    entry<SamplesNavKey.DigitalRoller>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DigitalRollerScreen(onBack)
    }
    entry<SamplesNavKey.DividingRule>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DividingRuleScreen(onBack)
    }
    entry<SamplesNavKey.DropCard>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DropCardScreen(onBack)
    }
    entry<SamplesNavKey.OrgTree>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        OrgTreeScreen(onBack)
    }
    entry<SamplesNavKey.IndexedList>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        IndexedListScreen(onBack)
    }
}