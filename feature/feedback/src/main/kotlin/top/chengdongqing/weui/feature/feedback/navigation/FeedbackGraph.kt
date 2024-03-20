package top.chengdongqing.weui.feature.feedback.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.feedback.screens.ActionSheetScreen
import top.chengdongqing.weui.feature.feedback.screens.ContextMenuScreen
import top.chengdongqing.weui.feature.feedback.screens.DialogScreen
import top.chengdongqing.weui.feature.feedback.screens.InformationBarScreen
import top.chengdongqing.weui.feature.feedback.screens.PopupScreen
import top.chengdongqing.weui.feature.feedback.screens.ToastScreen

fun NavGraphBuilder.addFeedbackGraph() {
    composable("dialog") {
        DialogScreen()
    }
    composable("popup") {
        PopupScreen()
    }
    composable("action_sheet") {
        ActionSheetScreen()
    }
    composable("toast") {
        ToastScreen()
    }
    composable("information_bar") {
        InformationBarScreen()
    }
    composable("context_menu") {
        ContextMenuScreen()
    }
}