package top.chengdongqing.weui.feature.feedback.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.feature.feedback.ui.ActionSheetScreen
import top.chengdongqing.weui.feature.feedback.ui.ContextMenuScreen
import top.chengdongqing.weui.feature.feedback.ui.DialogScreen
import top.chengdongqing.weui.feature.feedback.ui.InformationBarScreen
import top.chengdongqing.weui.feature.feedback.ui.PopupScreen
import top.chengdongqing.weui.feature.feedback.ui.ToastScreen
import top.chengdongqing.weui.navigation.FeedbackNavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.feedbackNavEntries(onBack: () -> Unit) {
    entry<FeedbackNavKey.ActionSheet>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ActionSheetScreen(onBack)
    }
    entry<FeedbackNavKey.ContextMenu>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ContextMenuScreen(onBack)
    }
    entry<FeedbackNavKey.Dialog>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DialogScreen(onBack)
    }
    entry<FeedbackNavKey.InformationBar>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        InformationBarScreen(onBack)
    }
    entry<FeedbackNavKey.Popup>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        PopupScreen(onBack)
    }
    entry<FeedbackNavKey.Toast>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ToastScreen(onBack)
    }
}