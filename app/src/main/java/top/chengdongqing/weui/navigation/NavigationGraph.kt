package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.ui.views.HomePage
import top.chengdongqing.weui.ui.views.basic.BadgePage
import top.chengdongqing.weui.ui.views.basic.LoadMorePage
import top.chengdongqing.weui.ui.views.basic.LoadingPage
import top.chengdongqing.weui.ui.views.basic.ProgressPage
import top.chengdongqing.weui.ui.views.device.CalendarPage
import top.chengdongqing.weui.ui.views.device.CallAndContactsPage
import top.chengdongqing.weui.ui.views.device.ClipboardPage
import top.chengdongqing.weui.ui.views.device.DatabasePage
import top.chengdongqing.weui.ui.views.device.DeviceInfoPage
import top.chengdongqing.weui.ui.views.device.FlashlightPage
import top.chengdongqing.weui.ui.views.device.InfraredPage
import top.chengdongqing.weui.ui.views.device.ScreenBrightnessPage
import top.chengdongqing.weui.ui.views.device.SmsPage
import top.chengdongqing.weui.ui.views.device.SystemStatusPage
import top.chengdongqing.weui.ui.views.device.VibrationPage
import top.chengdongqing.weui.ui.views.device.WiFiPage
import top.chengdongqing.weui.ui.views.feedback.ActionSheetPage
import top.chengdongqing.weui.ui.views.feedback.DialogPage
import top.chengdongqing.weui.ui.views.feedback.InformationBarPage
import top.chengdongqing.weui.ui.views.feedback.PopupPage
import top.chengdongqing.weui.ui.views.feedback.ToastPage
import top.chengdongqing.weui.ui.views.form.ButtonPage
import top.chengdongqing.weui.ui.views.form.CheckboxPage
import top.chengdongqing.weui.ui.views.form.RadioPage
import top.chengdongqing.weui.ui.views.form.SliderPage
import top.chengdongqing.weui.ui.views.form.SwitchPage
import top.chengdongqing.weui.ui.views.media.GalleryPage
import top.chengdongqing.weui.ui.views.search.SearchBarPage

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

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
        deviceGraph()
        searchGraph()
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
    composable("gallery") {
        GalleryPage()
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
    composable("call-contacts") {
        CallAndContactsPage()
    }
    composable("sms") {
        SmsPage()
    }
    composable("calendar") {
        CalendarPage()
    }
}

fun NavGraphBuilder.searchGraph() {
    composable("search-bar") {
        SearchBarPage()
    }
}