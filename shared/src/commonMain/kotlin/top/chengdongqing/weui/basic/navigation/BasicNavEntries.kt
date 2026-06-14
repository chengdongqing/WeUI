package top.chengdongqing.weui.basic.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.basic.ui.BadgeScreen
import top.chengdongqing.weui.basic.ui.LoadMoreScreen
import top.chengdongqing.weui.basic.ui.LoadingScreen
import top.chengdongqing.weui.basic.ui.ProgressScreen
import top.chengdongqing.weui.basic.ui.RefreshViewScreen
import top.chengdongqing.weui.navigation.BasicNavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.basicNavEntries(onBack: () -> Unit) {
    entry<BasicNavKey.Badge>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        BadgeScreen(onBack)
    }
    entry<BasicNavKey.Loading>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        LoadingScreen(onBack)
    }
    entry<BasicNavKey.LoadMore>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        LoadMoreScreen(onBack)
    }
    entry<BasicNavKey.Progress>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ProgressScreen(onBack)
    }
    entry<BasicNavKey.RefreshView>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        RefreshViewScreen(onBack)
    }
}