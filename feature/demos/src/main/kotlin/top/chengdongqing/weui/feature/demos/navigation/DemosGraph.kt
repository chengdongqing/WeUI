package top.chengdongqing.weui.feature.demos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.demos.filebrowser.FileBrowserScreen
import top.chengdongqing.weui.feature.demos.paint.PaintScreen
import top.chengdongqing.weui.feature.demos.screens.CalendarScreen
import top.chengdongqing.weui.feature.demos.screens.ClockScreen
import top.chengdongqing.weui.feature.demos.screens.CubicBezierScreen
import top.chengdongqing.weui.feature.demos.screens.DigitalKeyboardScreen
import top.chengdongqing.weui.feature.demos.screens.DigitalRollerScreen
import top.chengdongqing.weui.feature.demos.screens.DividingRuleScreen
import top.chengdongqing.weui.feature.demos.screens.DragSorterScreen
import top.chengdongqing.weui.feature.demos.screens.DropCardScreen
import top.chengdongqing.weui.feature.demos.screens.IndexedListScreen
import top.chengdongqing.weui.feature.demos.screens.NotificationBarScreen
import top.chengdongqing.weui.feature.demos.screens.OrgTreeScreen
import top.chengdongqing.weui.feature.demos.screens.SearchBarScreen
import top.chengdongqing.weui.feature.demos.screens.SolarSystemScreen
import top.chengdongqing.weui.feature.demos.videochannel.VideoChannelScreen

fun NavGraphBuilder.addDemosGraph() {
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
    composable("drag_sorter") {
        DragSorterScreen()
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