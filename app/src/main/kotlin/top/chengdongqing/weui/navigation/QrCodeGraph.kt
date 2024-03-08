package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.qrcode.generator.QrCodeGenerateScreen
import top.chengdongqing.weui.ui.screens.qrcode.scanner.QrCodeScanScreen

fun NavGraphBuilder.addQrCodeGraph(navController: NavController) {
    composable("qrcode-scanner") {
        QrCodeScanScreen(navController)
    }
    composable("qrcode-generator") {
        QrCodeGenerateScreen()
    }
}