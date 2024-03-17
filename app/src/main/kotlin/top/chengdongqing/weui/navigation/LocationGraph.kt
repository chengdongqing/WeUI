package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.location.LocationPickerScreen
import top.chengdongqing.weui.ui.screens.location.LocationPreviewScreen

fun NavGraphBuilder.addLocationGraph() {
    composable("location-preview") {
        LocationPreviewScreen()
    }
    composable("location-picker") {
        LocationPickerScreen()
    }
}