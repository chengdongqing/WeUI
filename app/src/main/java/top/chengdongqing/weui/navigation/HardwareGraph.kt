package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import top.chengdongqing.weui.ui.views.hardware.AccelerometerPage
import top.chengdongqing.weui.ui.views.hardware.BluetoothPage
import top.chengdongqing.weui.ui.views.hardware.CompassPage
import top.chengdongqing.weui.ui.views.hardware.FingerprintPage
import top.chengdongqing.weui.ui.views.hardware.FlashlightPage
import top.chengdongqing.weui.ui.views.hardware.GPSPage
import top.chengdongqing.weui.ui.views.hardware.GyroscopePage
import top.chengdongqing.weui.ui.views.hardware.InfraredPage
import top.chengdongqing.weui.ui.views.hardware.NFCPage
import top.chengdongqing.weui.ui.views.hardware.ScreenPage
import top.chengdongqing.weui.ui.views.hardware.VibrationPage
import top.chengdongqing.weui.ui.views.hardware.WiFiPage

fun NavGraphBuilder.addHardwareGraph() {
    navigation("screen", "hardware") {
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
        composable("gps") {
            GPSPage()
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
        composable("fingerprint") {
            FingerprintPage()
        }
    }
}