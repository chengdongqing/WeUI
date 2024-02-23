package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.demo.CalendarScreen
import top.chengdongqing.weui.ui.screens.demo.ClockScreen
import top.chengdongqing.weui.ui.screens.demo.CubicBezierScreen
import top.chengdongqing.weui.ui.screens.demo.DigitalKeyboardScreen
import top.chengdongqing.weui.ui.screens.demo.DigitalRollerScreen
import top.chengdongqing.weui.ui.screens.demo.DividingRuleScreen
import top.chengdongqing.weui.ui.screens.demo.DragSorterScreen
import top.chengdongqing.weui.ui.screens.demo.IndexedListScreen
import top.chengdongqing.weui.ui.screens.demo.NotificationBarScreen
import top.chengdongqing.weui.ui.screens.demo.OrgTreeScreen
import top.chengdongqing.weui.ui.screens.demo.SearchBarScreen
import top.chengdongqing.weui.ui.screens.demo.TreeViewScreen
import top.chengdongqing.weui.ui.screens.demo.filebrowser.FileBrowserScreen
import top.chengdongqing.weui.ui.screens.demo.gallery.GalleryScreen
import top.chengdongqing.weui.ui.screens.demo.paint.PaintScreen

fun NavGraphBuilder.addDemoGraph() {
    composable("search-bar") {
        SearchBarScreen()
    }
    composable("calendar") {
        CalendarScreen()
    }
    composable("clock") {
        ClockScreen()
    }
    composable("gallery") {
        GalleryScreen()
    }
    composable("file-browser") {
        FileBrowserScreen()
    }
    composable("paint") {
        PaintScreen()
    }
    composable("indexed-list") {
        IndexedListScreen()
    }
    composable("drag-sorter") {
        DragSorterScreen()
    }
    composable("dividing-rule") {
        DividingRuleScreen()
    }
    composable("org-tree") {
        OrgTreeScreen()
    }
    composable("tree-view") {
        TreeViewScreen()
    }
    composable("digital-roller") {
        DigitalRollerScreen()
    }
    composable("digital-keyboard") {
        DigitalKeyboardScreen()
    }
    composable("cubic-bezier") {
        CubicBezierScreen()
    }
    composable("notification-bar") {
        NotificationBarScreen()
    }
}