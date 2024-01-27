package top.chengdongqing.weui.navigation

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

fun NavGraphBuilder.systemGraph() {
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
        DatabasePage()
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