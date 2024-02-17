package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.hardware.AccelerometerPage
import top.chengdongqing.weui.ui.views.hardware.BluetoothPage
import top.chengdongqing.weui.ui.views.hardware.FingerprintPage
import top.chengdongqing.weui.ui.views.hardware.FlashlightPage
import top.chengdongqing.weui.ui.views.hardware.GNSSPage
import top.chengdongqing.weui.ui.views.hardware.GyroscopePage
import top.chengdongqing.weui.ui.views.hardware.HygrothermographPage
import top.chengdongqing.weui.ui.views.hardware.InfraredPage
import top.chengdongqing.weui.ui.views.hardware.NFCPage
import top.chengdongqing.weui.ui.views.hardware.ScreenPage
import top.chengdongqing.weui.ui.views.hardware.VibrationPage
import top.chengdongqing.weui.ui.views.hardware.WiFiPage
import top.chengdongqing.weui.ui.views.hardware.compass.CompassPage

fun NavGraphBuilder.addHardwareGraph() {
    composable("screen") {
        ScreenPage()
    }
    composable("flashlight") {
        FlashlightPage()
    }
    composable("vibration") {
        VibrationPage()
    }
    composable("wifi") {
        WiFiPage()
    }
    composable("bluetooth") {
        BluetoothPage()
    }
    composable("nfc") {
        NFCPage()
    }
    composable("gnss") {
        GNSSPage()
    }
    composable("infrared") {
        InfraredPage()
    }
    composable("gyroscope") {
        GyroscopePage()
    }
    composable("compass") {
        CompassPage()
    }
    composable("accelerometer") {
        AccelerometerPage()
    }
    composable("hygrothermograph") {
        HygrothermographPage()
    }
    composable("fingerprint") {
        FingerprintPage()
    }
}