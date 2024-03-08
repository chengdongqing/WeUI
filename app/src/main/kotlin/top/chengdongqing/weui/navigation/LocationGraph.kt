package top.chengdongqing.weui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.components.location.picker.LocationItem
import top.chengdongqing.weui.ui.components.location.picker.WeLocationPicker
import top.chengdongqing.weui.ui.components.location.preview.WeLocationPreview
import top.chengdongqing.weui.ui.screens.location.LocationPickerScreen
import top.chengdongqing.weui.ui.screens.location.LocationPreviewScreen

fun NavGraphBuilder.addLocationGraph(navController: NavHostController) {
    composable("location-preview") {
        LocationPreviewScreen(navController)
    }
    composable("location-picker") {
        val locationState = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<LocationItem?>("location", null)
            ?.collectAsState()
        LocationPickerScreen(navController, locationState)
    }

    composable(route = "location/preview/{latitude}/{longitude}?zoom={zoom}&name={name}&address={address}") {
        val args = it.arguments!!
        val latitude = args.getString("latitude")!!.toDouble()
        val longitude = args.getString("longitude")!!.toDouble()
        val zoom = args.getString("zoom")?.toFloat() ?: 16f
        val name = args.getString("name", "位置")
        val address = args.getString("address")
        WeLocationPreview(latitude, longitude, zoom, name, address)
    }
    composable("location/pick") {
        WeLocationPicker(onCancel = {
            navController.popBackStack()
        }) {
            navController.previousBackStackEntry?.savedStateHandle?.set("location", it)
            navController.popBackStack()
        }
    }
}