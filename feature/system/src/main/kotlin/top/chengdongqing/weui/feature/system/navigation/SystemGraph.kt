package top.chengdongqing.weui.feature.system.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.system.address.AddressFormScreen
import top.chengdongqing.weui.feature.system.screens.CalendarEventsScreen
import top.chengdongqing.weui.feature.system.screens.ClipboardScreen
import top.chengdongqing.weui.feature.system.screens.ContactsScreen
import top.chengdongqing.weui.feature.system.screens.DatabaseScreen
import top.chengdongqing.weui.feature.system.screens.DeviceInfoScreen
import top.chengdongqing.weui.feature.system.screens.DownloaderScreen
import top.chengdongqing.weui.feature.system.screens.InstalledAppsScreen
import top.chengdongqing.weui.feature.system.screens.KeyboardScreen
import top.chengdongqing.weui.feature.system.screens.NotificationScreen
import top.chengdongqing.weui.feature.system.screens.SmsScreen
import top.chengdongqing.weui.feature.system.screens.SystemStatusScreen

fun NavGraphBuilder.addSystemGraph(navController: NavController) {
    composable("device_info") {
        DeviceInfoScreen()
    }
    composable("system_status") {
        SystemStatusScreen()
    }
    composable("installed_apps") {
        InstalledAppsScreen()
    }
    composable("downloader") {
        DownloaderScreen()
    }
    composable("database") {
        DatabaseScreen(navController)
    }
    composable("address_form?id={id}") {
        val id = it.arguments?.getString("id")?.toInt()
        AddressFormScreen(navController, id)
    }
    composable("clipboard") {
        ClipboardScreen()
    }
    composable("contacts") {
        ContactsScreen()
    }
    composable("sms") {
        SmsScreen()
    }
    composable("keyboard") {
        KeyboardScreen()
    }
    composable("calendar_events") {
        CalendarEventsScreen()
    }
    composable("notification") {
        NotificationScreen()
    }
}