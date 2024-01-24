package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.basic.BadgePage
import top.chengdongqing.weui.ui.views.basic.LoadMorePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.basic.ProgressPage
import top.chengdongqing.weui.ui.views.basic.StepsPage
import top.chengdongqing.weui.ui.views.basic.SwiperPage
import top.chengdongqing.weui.ui.views.device.CalendarPage
import top.chengdongqing.weui.ui.views.device.ClipboardPage
import top.chengdongqing.weui.ui.views.device.ContactsPage
import top.chengdongqing.weui.ui.views.device.DatabasePage
import top.chengdongqing.weui.ui.views.device.DeviceInfoPage
import top.chengdongqing.weui.ui.views.device.DownloaderPage
import top.chengdongqing.weui.ui.views.device.FlashlightPage
import top.chengdongqing.weui.ui.views.device.InfraredPage
import top.chengdongqing.weui.ui.views.device.InstalledAppsPage
import top.chengdongqing.weui.ui.views.device.KeyboardPage
import top.chengdongqing.weui.ui.views.device.NotificationPage
import top.chengdongqing.weui.ui.views.device.ScreenBrightnessPage
import top.chengdongqing.weui.ui.views.device.SmsPage
import top.chengdongqing.weui.ui.views.device.SystemStatusPage
import top.chengdongqing.weui.ui.views.device.VibrationPage
import top.chengdongqing.weui.ui.views.device.WiFiPage
import top.chengdongqing.weui.ui.views.feedback.ActionSheetPage
import top.chengdongqing.weui.ui.views.feedback.ContextMenuPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.feedback.InformationBarPage
import top.chengdongqing.weui.ui.views.feedback.PopupPage
import top.chengdongqing.weui.ui.views.feedback.PullDownRefreshPage
import top.chengdongqing.weui.ui.views.feedback.ToastPage
import top.chengdongqing.weui.ui.views.form.ButtonPage
import top.chengdongqing.weui.ui.views.form.CheckboxPage
import top.chengdongqing.weui.ui.views.form.InputPage
import top.chengdongqing.weui.ui.views.form.PickerPage
import top.chengdongqing.weui.ui.views.form.RadioPage
import top.chengdongqing.weui.ui.views.form.SliderPage
import top.chengdongqing.weui.ui.views.form.SwitchPage
import top.chengdongqing.weui.ui.views.layers.LayersPage
import top.chengdongqing.weui.ui.views.media.file.FileBrowserPage
import top.chengdongqing.weui.ui.views.media.gallery.GalleryPage
import top.chengdongqing.weui.ui.views.media.gallery.GalleryViewModel
import top.chengdongqing.weui.ui.views.media.gallery.MediaPreviewPage
import top.chengdongqing.weui.ui.views.search.SearchBarPage

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val galleryViewModel: GalleryViewModel = viewModel()

    NavHost(
        navController,
        startDestination = "home",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("home",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        ) {
            HomePage(navController)
        }

        basicGraph()
        formGraph()
        feedbackGraph()
        mediaGraph(navController, galleryViewModel)
        deviceGraph()
        searchGraph()
        layersGraph()
    }
}

fun NavGraphBuilder.basicGraph() {
    composable("badge") {
        BadgePage()
    }
    composable("loading") {
        LoadingPage()
    }
    composable("load-more") {
        LoadMorePage()
    }
    composable("progress") {
        ProgressPage()
    }
    composable("steps") {
        StepsPage()
    }
    composable("swiper") {
        SwiperPage()
    }
}

fun NavGraphBuilder.formGraph() {
    composable("button") {
        ButtonPage()
    }
    composable("checkbox") {
        CheckboxPage()
    }
    composable("radio") {
        RadioPage()
    }
    composable("switch") {
        SwitchPage()
    }
    composable("slider") {
        SliderPage()
    }
    composable("picker") {
        PickerPage()
    }
    composable("input") {
        InputPage()
    }
}

fun NavGraphBuilder.feedbackGraph() {
    composable("dialog") {
        DialogPage()
    }
    composable("popup") {
        PopupPage()
    }
    composable("action-sheet") {
        ActionSheetPage()
    }
    composable("toast") {
        ToastPage()
    }
    composable("information-bar") {
        InformationBarPage()
    }
    composable("context-menu") {
        ContextMenuPage()
    }
    composable("pull-down-refresh") {
        PullDownRefreshPage()
    }
}

fun NavGraphBuilder.mediaGraph(navController: NavController, galleryViewModel: GalleryViewModel) {
    composable("gallery",
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {
        GalleryPage(galleryViewModel, navController)
    }
    composable("media-preview?index={index}",
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {
        MediaPreviewPage(galleryViewModel, navController)
    }
    composable("file-browser") {
        FileBrowserPage()
    }
}

fun NavGraphBuilder.deviceGraph() {
    composable("device-info") {
        DeviceInfoPage()
    }
    composable("system-status") {
        SystemStatusPage()
    }
    composable("database") {
        DatabasePage()
    }
    composable("wifi") {
        WiFiPage()
    }
    composable("bluetooth") {

    }
    composable("nfc") {

    }
    composable("flashlight") {
        FlashlightPage()
    }
    composable("infrared") {
        InfraredPage()
    }
    composable("clipboard") {
        ClipboardPage()
    }
    composable("vibration") {
        VibrationPage()
    }
    composable("screen-brightness") {
        ScreenBrightnessPage()
    }
    composable("contacts") {
        ContactsPage()
    }
    composable("sms") {
        SmsPage()
    }
    composable("calendar") {
        CalendarPage()
    }
    composable("notification") {
        NotificationPage()
    }
    composable("keyboard") {
        KeyboardPage()
    }
    composable("installed-apps") {
        InstalledAppsPage()
    }
    composable("downloader") {
        DownloaderPage()
    }
}

fun NavGraphBuilder.searchGraph() {
    composable("search-bar") {
        SearchBarPage()
    }
}

fun NavGraphBuilder.layersGraph() {
    composable("layers") {
        LayersPage()
    }
}