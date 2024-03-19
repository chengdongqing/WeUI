package top.chengdongqing.weui.feature.qrcode.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.qrcode.screens.QrCodeGeneratorScreen
import top.chengdongqing.weui.feature.qrcode.screens.QrCodeScanScreen

fun NavGraphBuilder.addQrCodeGraph() {
    composable("qrcode_scanner") {
        QrCodeScanScreen()
    }
    composable("qrcode_generator") {
        QrCodeGeneratorScreen()
    }
}