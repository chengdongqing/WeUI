package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.feedback.ActionSheetPage
import top.chengdongqing.weui.ui.views.feedback.ContextMenuPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.feedback.InformationBarPage
import top.chengdongqing.weui.ui.views.feedback.PopupPage
import top.chengdongqing.weui.ui.views.feedback.ToastPage

fun NavGraphBuilder.addFeedbackGraph() {
    composable("dialog") {
        DialogPage()
    }
    composable("popup") {
        PopupPage()
    }
    composable("action-sheet") {
        ActionSheetPage()
    }
    composable("toast") {
        ToastPage()
    }
    composable("information-bar") {
        InformationBarPage()
    }
    composable("context-menu") {
        ContextMenuPage()
    }
}