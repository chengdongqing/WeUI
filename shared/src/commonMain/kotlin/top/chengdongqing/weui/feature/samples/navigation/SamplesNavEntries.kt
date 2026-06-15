package top.chengdongqing.weui.feature.samples.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.feature.samples.ui.CalendarScreen
import top.chengdongqing.weui.feature.samples.ui.CubicBezierScreen
import top.chengdongqing.weui.feature.samples.ui.DigitalKeyboardScreen
import top.chengdongqing.weui.feature.samples.ui.DigitalRollerScreen
import top.chengdongqing.weui.feature.samples.ui.DividingRuleScreen
import top.chengdongqing.weui.feature.samples.ui.DropCardScreen
import top.chengdongqing.weui.feature.samples.ui.IndexedListScreen
import top.chengdongqing.weui.feature.samples.ui.NotificationBarScreen
import top.chengdongqing.weui.feature.samples.ui.OrgTreeScreen
import top.chengdongqing.weui.feature.samples.ui.PanoramicImageScreen
import top.chengdongqing.weui.feature.samples.ui.ReorderableScreen
import top.chengdongqing.weui.feature.samples.ui.SearchBarScreen
import top.chengdongqing.weui.feature.samples.ui.paint.PaintScreen
import top.chengdongqing.weui.navigation.SamplesNavKey

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
    entry<SamplesNavKey.SearchBar>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SearchBarScreen(onBack)
    }
    entry<SamplesNavKey.Reorderable>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ReorderableScreen(onBack)
    }
    entry<SamplesNavKey.Paint>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        WeUITheme(isDark = false) {
            PaintScreen(onBack)
        }
    }
    entry<SamplesNavKey.Calendar>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        CalendarScreen(onBack)
    }
}