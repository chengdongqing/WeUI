package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.feedback.ActionSheetScreen
import top.chengdongqing.weui.ui.screens.feedback.ContextMenuScreen
import top.chengdongqing.weui.ui.screens.feedback.DialogScreen
import top.chengdongqing.weui.ui.screens.feedback.InformationBarScreen
import top.chengdongqing.weui.ui.screens.feedback.PopupScreen
import top.chengdongqing.weui.ui.screens.feedback.ToastScreen

fun NavGraphBuilder.addFeedbackGraph() {
    composable("dialog") {
        DialogScreen()
    }
    composable("popup") {
        PopupScreen()
    }
    composable("action-sheet") {
        ActionSheetScreen()
    }
    composable("toast") {
        ToastScreen()
    }
    composable("information-bar") {
        InformationBarScreen()
    }
    composable("context-menu") {
        ContextMenuScreen()
    }
}