package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.system.CalendarPage
import top.chengdongqing.weui.ui.views.system.ClipboardPage
import top.chengdongqing.weui.ui.views.system.ContactsPage
import top.chengdongqing.weui.ui.views.system.DeviceInfoPage
import top.chengdongqing.weui.ui.views.system.DownloaderPage
import top.chengdongqing.weui.ui.views.system.InstalledAppsPage
import top.chengdongqing.weui.ui.views.system.KeyboardPage
import top.chengdongqing.weui.ui.views.system.NotificationPage
import top.chengdongqing.weui.ui.views.system.SmsPage
import top.chengdongqing.weui.ui.views.system.SystemStatusPage
import top.chengdongqing.weui.ui.views.system.database.DatabasePage
import top.chengdongqing.weui.ui.views.system.database.address.AddressFormPage

fun NavGraphBuilder.systemGraph(navController: NavController) {
    composable("device-info") {
        DeviceInfoPage()
    }
    composable("system-status") {
        SystemStatusPage()
    }
    composable("installed-apps") {
        InstalledAppsPage()
    }
    composable("downloader") {
        DownloaderPage()
    }
    composable("database") {
        DatabasePage(navController)
    }
    composable("address-form?id={id}") {
        val id = it.arguments?.getString("id")?.toInt()
        AddressFormPage(navController, id)
    }
    composable("clipboard") {
        ClipboardPage()
    }
    composable("contacts") {
        ContactsPage()
    }
    composable("sms") {
        SmsPage()
    }
    composable("keyboard") {
        KeyboardPage()
    }
    composable("calendar") {
        CalendarPage()
    }
    composable("notification") {
        NotificationPage()
    }
}