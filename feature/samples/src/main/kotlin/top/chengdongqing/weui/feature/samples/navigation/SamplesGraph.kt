package top.chengdongqing.weui.feature.samples.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.samples.filebrowser.FileBrowserScreen
import top.chengdongqing.weui.feature.samples.paint.PaintScreen
import top.chengdongqing.weui.feature.samples.screens.CalendarScreen
import top.chengdongqing.weui.feature.samples.screens.ClockScreen
import top.chengdongqing.weui.feature.samples.screens.CubicBezierScreen
import top.chengdongqing.weui.feature.samples.screens.DigitalKeyboardScreen
import top.chengdongqing.weui.feature.samples.screens.DigitalRollerScreen
import top.chengdongqing.weui.feature.samples.screens.DividingRuleScreen
import top.chengdongqing.weui.feature.samples.screens.DropCardScreen
import top.chengdongqing.weui.feature.samples.screens.IndexedListScreen
import top.chengdongqing.weui.feature.samples.screens.NotificationBarScreen
import top.chengdongqing.weui.feature.samples.screens.OrgTreeScreen
import top.chengdongqing.weui.feature.samples.screens.ReorderableScreen
import top.chengdongqing.weui.feature.samples.screens.SearchBarScreen
import top.chengdongqing.weui.feature.samples.screens.SolarSystemScreen
import top.chengdongqing.weui.feature.samples.videochannel.VideoChannelScreen

fun NavGraphBuilder.addSamplesGraph() {
    composable("search_bar") {
        SearchBarScreen()
    }
    composable("calendar") {
        CalendarScreen()
    }
    composable("clock") {
        ClockScreen()
    }
    composable("drop_card") {
        DropCardScreen()
    }
    composable("file_browser") {
        FileBrowserScreen()
    }
    composable("paint") {
        PaintScreen()
    }
    composable("indexed_list") {
        IndexedListScreen()
    }
    composable("reorderable") {
        ReorderableScreen()
    }
    composable("dividing_rule") {
        DividingRuleScreen()
    }
    composable("org_tree") {
        OrgTreeScreen()
    }
    composable("digital_roller") {
        DigitalRollerScreen()
    }
    composable("digital_keyboard") {
        DigitalKeyboardScreen()
    }
    composable("cubic_bezier") {
        CubicBezierScreen()
    }
    composable("notification_bar") {
        NotificationBarScreen()
    }
    composable("video_channel") {
        VideoChannelScreen()
    }
    composable("solar_system") {
        SolarSystemScreen()
    }
}