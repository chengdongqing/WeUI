package top.chengdongqing.weui.feature.location.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.location.screens.LocationPickerScreen
import top.chengdongqing.weui.feature.location.screens.LocationPreviewScreen

fun NavGraphBuilder.addLocationGraph() {
    composable("location_preview") {
        LocationPreviewScreen()
    }
    composable("location_picker") {
        LocationPickerScreen()
    }
}