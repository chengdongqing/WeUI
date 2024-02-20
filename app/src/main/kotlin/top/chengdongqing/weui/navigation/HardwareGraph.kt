package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.hardware.AccelerometerScreen
import top.chengdongqing.weui.ui.screens.hardware.BluetoothScreen
import top.chengdongqing.weui.ui.screens.hardware.FingerprintScreen
import top.chengdongqing.weui.ui.screens.hardware.FlashlightScreen
import top.chengdongqing.weui.ui.screens.hardware.GNSSScreen
import top.chengdongqing.weui.ui.screens.hardware.GyroscopeScreen
import top.chengdongqing.weui.ui.screens.hardware.HygrothermographScreen
import top.chengdongqing.weui.ui.screens.hardware.InfraredScreen
import top.chengdongqing.weui.ui.screens.hardware.NFCScreen
import top.chengdongqing.weui.ui.screens.hardware.ScreenScreen
import top.chengdongqing.weui.ui.screens.hardware.VibrationScreen
import top.chengdongqing.weui.ui.screens.hardware.WiFiScreen
import top.chengdongqing.weui.ui.screens.hardware.compass.CompassScreen

fun NavGraphBuilder.addHardwareGraph() {
    composable("screen") {
        ScreenScreen()
    }
    composable("flashlight") {
        FlashlightScreen()
    }
    composable("vibration") {
        VibrationScreen()
    }
    composable("wifi") {
        WiFiScreen()
    }
    composable("bluetooth") {
        BluetoothScreen()
    }
    composable("nfc") {
        NFCScreen()
    }
    composable("gnss") {
        GNSSScreen()
    }
    composable("infrared") {
        InfraredScreen()
    }
    composable("gyroscope") {
        GyroscopeScreen()
    }
    composable("compass") {
        CompassScreen()
    }
    composable("accelerometer") {
        AccelerometerScreen()
    }
    composable("hygrothermograph") {
        HygrothermographScreen()
    }
    composable("fingerprint") {
        FingerprintScreen()
    }
}