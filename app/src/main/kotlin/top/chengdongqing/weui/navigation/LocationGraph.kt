package top.chengdongqing.weui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.components.location.picker.LocationItem
import top.chengdongqing.weui.ui.components.location.picker.WeLocationPicker
import top.chengdongqing.weui.ui.components.location.preview.WeLocationPreview
import top.chengdongqing.weui.ui.screens.location.LocationPickerScreen
import top.chengdongqing.weui.ui.screens.location.LocationPreviewScreen

fun NavGraphBuilder.addLocationGraph(navController: NavHostController) {
    composable("location-preview/entrance") {
        LocationPreviewScreen(navController)
    }
    composable("location-picker/entrance") { backStackEntry ->
        val location by backStackEntry.savedStateHandle
            .getStateFlow<LocationItem?>("location", null)
            .collectAsState()

        LocationPickerScreen(location) {
            backStackEntry.savedStateHandle.remove<LocationItem?>("location")
            navController.navigate("location-picker")
        }
    }

    composable(route = "location-preview/{latitude}/{longitude}?zoom={zoom}&name={name}&address={address}") { navBackStackEntry ->
        val args = navBackStackEntry.arguments!!
        val latitude = args.getString("latitude")!!.toDouble()
        val longitude = args.getString("longitude")!!.toDouble()
        val zoom = args.getString("zoom")?.toFloat() ?: 16f
        val name = args.getString("name", "位置")
        val address = args.getString("address")

        WeLocationPreview(latitude, longitude, zoom, name, address)
    }
    composable("location-picker") {
        WeLocationPicker(onCancel = {
            navController.popBackStack()
        }) {
            navController.previousBackStackEntry?.savedStateHandle?.set("location", it)
            navController.popBackStack()
        }
    }
}