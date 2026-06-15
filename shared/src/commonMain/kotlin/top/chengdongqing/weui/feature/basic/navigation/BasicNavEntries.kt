package top.chengdongqing.weui.feature.basic.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.feature.basic.ui.BadgeScreen
import top.chengdongqing.weui.feature.basic.ui.LoadMoreScreen
import top.chengdongqing.weui.feature.basic.ui.LoadingScreen
import top.chengdongqing.weui.feature.basic.ui.ProgressScreen
import top.chengdongqing.weui.feature.basic.ui.RefreshViewScreen
import top.chengdongqing.weui.feature.basic.ui.SkeletonScreen
import top.chengdongqing.weui.feature.basic.ui.StepsScreen
import top.chengdongqing.weui.feature.basic.ui.SwipeActionScreen
import top.chengdongqing.weui.feature.basic.ui.SwiperScreen
import top.chengdongqing.weui.feature.basic.ui.TabViewScreen
import top.chengdongqing.weui.feature.basic.ui.TreeScreen
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
    entry<BasicNavKey.Skeleton>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SkeletonScreen(onBack)
    }
    entry<BasicNavKey.Steps>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        StepsScreen(onBack)
    }
    entry<BasicNavKey.SwipeAction>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SwipeActionScreen(onBack)
    }
    entry<BasicNavKey.Swiper>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SwiperScreen(onBack)
    }
    entry<BasicNavKey.TabView>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        TabViewScreen(onBack)
    }
    entry<BasicNavKey.Tree>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        TreeScreen(onBack)
    }
}