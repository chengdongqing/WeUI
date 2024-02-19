package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.demo.calendar.CalendarPage
import top.chengdongqing.weui.ui.views.demo.clock.ClockPage
import top.chengdongqing.weui.ui.views.demo.filebrowser.FileBrowserPage
import top.chengdongqing.weui.ui.views.demo.gallery.GalleryPage
import top.chengdongqing.weui.ui.views.demo.indexedlist.IndexedListPage
import top.chengdongqing.weui.ui.views.demo.paint.PaintPage
import top.chengdongqing.weui.ui.views.demo.searchbar.SearchBarPage

fun NavGraphBuilder.addDemoGraph() {
    composable("search-bar") {
        SearchBarPage()
    }
    composable("calendar") {
        CalendarPage()
    }
    composable("clock") {
        ClockPage()
    }
    composable("gallery") {
        GalleryPage()
    }
    composable("file-browser") {
        FileBrowserPage()
    }
    composable("paint") {
        PaintPage()
    }
    composable("indexed-list") {
        IndexedListPage()
    }
}