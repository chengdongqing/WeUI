package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.demo.calendar.CalendarScreen
import top.chengdongqing.weui.ui.screens.demo.clock.ClockScreen
import top.chengdongqing.weui.ui.screens.demo.filebrowser.FileBrowserScreen
import top.chengdongqing.weui.ui.screens.demo.gallery.GalleryScreen
import top.chengdongqing.weui.ui.screens.demo.indexedlist.IndexedListScreen
import top.chengdongqing.weui.ui.screens.demo.paint.PaintScreen
import top.chengdongqing.weui.ui.screens.demo.searchbar.SearchBarScreen

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
}