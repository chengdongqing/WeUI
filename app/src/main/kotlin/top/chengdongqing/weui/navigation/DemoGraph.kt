package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.basic.DropCardScreen
import top.chengdongqing.weui.ui.screens.demo.CalendarScreen
import top.chengdongqing.weui.ui.screens.demo.ClockScreen
import top.chengdongqing.weui.ui.screens.demo.CubicBezierScreen
import top.chengdongqing.weui.ui.screens.demo.DigitalKeyboardScreen
import top.chengdongqing.weui.ui.screens.demo.DigitalRollerScreen
import top.chengdongqing.weui.ui.screens.demo.DividingRuleScreen
import top.chengdongqing.weui.ui.screens.demo.DragSorterScreen
import top.chengdongqing.weui.ui.screens.demo.NotificationBarScreen
import top.chengdongqing.weui.ui.screens.demo.PanoramicImageScreen
import top.chengdongqing.weui.ui.screens.demo.SearchBarScreen
import top.chengdongqing.weui.ui.screens.demo.SolarSystemScreen
import top.chengdongqing.weui.ui.screens.demo.filebrowser.FileBrowserScreen
import top.chengdongqing.weui.ui.screens.demo.gallery.GalleryScreen
import top.chengdongqing.weui.ui.screens.demo.imagecropper.ImageCropperScreen
import top.chengdongqing.weui.ui.screens.demo.indexedlist.IndexedListScreen
import top.chengdongqing.weui.ui.screens.demo.orgtree.OrgTreeScreen
import top.chengdongqing.weui.ui.screens.demo.paint.PaintScreen
import top.chengdongqing.weui.ui.screens.demo.videochannel.VideoChannelScreen

fun NavGraphBuilder.addDemoGraph(navController: NavController) {
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
    composable("gallery") {
        GalleryScreen(navController)
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
    composable("image_cropper") {
        ImageCropperScreen()
    }
    composable("video_channel") {
        VideoChannelScreen()
    }
    composable("solar_system") {
        SolarSystemScreen()
    }
    composable("panoramic_image") {
        PanoramicImageScreen()
    }
}