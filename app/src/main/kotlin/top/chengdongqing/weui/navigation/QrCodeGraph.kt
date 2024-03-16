package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.components.qrcode.scanner.WeQrCodeScanner
import top.chengdongqing.weui.ui.screens.qrcode.generator.QrCodeGeneratorScreen
import top.chengdongqing.weui.ui.screens.qrcode.scanner.QrCodeScanScreen

fun NavGraphBuilder.addQrCodeGraph(navController: NavController) {
    composable("qrcode-scanner") {
        WeQrCodeScanner {

        }
    }

    composable("qrcode-scanner/entrance") {
        QrCodeScanScreen(navController)
    }
    composable("qrcode-generator") {
        QrCodeGeneratorScreen()
    }
}