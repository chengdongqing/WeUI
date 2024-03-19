package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.location.LocationPickerScreen
import top.chengdongqing.weui.ui.screens.location.LocationPreviewScreen

fun NavGraphBuilder.addLocationGraph() {
    composable("location_preview") {
        LocationPreviewScreen()
    }
    composable("location_picker") {
        LocationPickerScreen()
    }
}