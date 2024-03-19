package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.qrcode.generator.QrCodeGeneratorScreen
import top.chengdongqing.weui.ui.screens.qrcode.scanner.QrCodeScanScreen

fun NavGraphBuilder.addQrCodeGraph() {
    composable("qrcode_scanner") {
        QrCodeScanScreen()
    }
    composable("qrcode_generator") {
        QrCodeGeneratorScreen()
    }
}