package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.template.calendar.CalendarPage
import top.chengdongqing.weui.ui.views.template.clock.ClockPage
import top.chengdongqing.weui.ui.views.template.filebrowser.FileBrowserPage
import top.chengdongqing.weui.ui.views.template.gallery.GalleryPage
import top.chengdongqing.weui.ui.views.template.indexedlist.IndexedListPage
import top.chengdongqing.weui.ui.views.template.paint.PaintPage
import top.chengdongqing.weui.ui.views.template.searchbar.SearchBarPage

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