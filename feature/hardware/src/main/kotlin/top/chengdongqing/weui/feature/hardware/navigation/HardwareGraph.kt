package top.chengdongqing.weui.feature.hardware.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.hardware.screens.AccelerometerScreen
import top.chengdongqing.weui.feature.hardware.screens.BluetoothScreen
import top.chengdongqing.weui.feature.hardware.screens.CompassScreen
import top.chengdongqing.weui.feature.hardware.screens.FingerprintScreen
import top.chengdongqing.weui.feature.hardware.screens.FlashlightScreen
import top.chengdongqing.weui.feature.hardware.screens.GNSSScreen
import top.chengdongqing.weui.feature.hardware.screens.GyroscopeScreen
import top.chengdongqing.weui.feature.hardware.screens.HygrothermographScreen
import top.chengdongqing.weui.feature.hardware.screens.InfraredScreen
import top.chengdongqing.weui.feature.hardware.screens.NFCScreen
import top.chengdongqing.weui.feature.hardware.screens.ScreenScreen
import top.chengdongqing.weui.feature.hardware.screens.VibrationScreen
import top.chengdongqing.weui.feature.hardware.screens.WiFiScreen

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