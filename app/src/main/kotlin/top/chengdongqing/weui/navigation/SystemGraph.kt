package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.system.CalendarEventsScreen
import top.chengdongqing.weui.ui.screens.system.ClipboardScreen
import top.chengdongqing.weui.ui.screens.system.ContactsScreen
import top.chengdongqing.weui.ui.screens.system.DeviceInfoScreen
import top.chengdongqing.weui.ui.screens.system.DownloaderScreen
import top.chengdongqing.weui.ui.screens.system.InstalledAppsScreen
import top.chengdongqing.weui.ui.screens.system.KeyboardScreen
import top.chengdongqing.weui.ui.screens.system.NotificationScreen
import top.chengdongqing.weui.ui.screens.system.SmsScreen
import top.chengdongqing.weui.ui.screens.system.SystemStatusScreen
import top.chengdongqing.weui.ui.screens.system.database.DatabaseScreen
import top.chengdongqing.weui.ui.screens.system.database.address.AddressFormScreen

fun NavGraphBuilder.addSystemGraph(navController: NavController) {
    composable("device-info") {
        DeviceInfoScreen()
    }
    composable("system-status") {
        SystemStatusScreen()
    }
    composable("installed-apps") {
        InstalledAppsScreen()
    }
    composable("downloader") {
        DownloaderScreen()
    }
    composable("database") {
        DatabaseScreen(navController)
    }
    composable("address-form?id={id}") {
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
    composable("calendar-events") {
        CalendarEventsScreen()
    }
    composable("notification") {
        NotificationScreen()
    }
}